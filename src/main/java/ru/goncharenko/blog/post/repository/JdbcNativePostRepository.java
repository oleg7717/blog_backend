package ru.goncharenko.blog.post.repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.model.Post;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcNativePostRepository implements PostRepository {
	private final JdbcTemplate jdbcTemplate;
	private final String SELECT_FROM_POST = "select p.* from posts p ";
	private final String SEARCH_BY_TAGS = """
			p.id in (
			    select postid
			    from tags
			    where tagname = any(?)
			    group by postid
			    having count(distinct tagname) = ?
			)
			""";
	private final String SERCH_BY_STRING = "p.title like ? ";
	private final String ORDER_BY = "order by p.id limit ? offset ?";

	public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Long recordsCount() {
		Long count = jdbcTemplate.queryForObject("select count(id) from posts", Long.class);
		return count != null ? count : 0;
	}

	@Override
	public List<Post> getRecords(int limit, int offset) {
		return jdbcTemplate.query(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(SELECT_FROM_POST + ORDER_BY);
					ps.setInt(1, limit);
					ps.setInt(2, offset);
					return ps;
				},
				map()
		);
	}

	@Override
	public List<Post> searchByTagsAndSubstring(String search, int tagsCount, List<String> tags, int limit, int offset) {
		return jdbcTemplate.query(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(
							SELECT_FROM_POST + "where " +
									SEARCH_BY_TAGS + " and " +
									SERCH_BY_STRING +
									ORDER_BY);
					ps.setArray(1, connection.createArrayOf("text", tags.toArray()));
					ps.setInt(2, tagsCount);
					ps.setString(3, "%" + search + "%");
					ps.setInt(4, limit);
					ps.setInt(5, offset);
					return ps;
				},
				map()
		);
	}

	@Override
	public List<Post> searchBySubstring(String search, int limit, int offset) {
		return jdbcTemplate.query(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(
							SELECT_FROM_POST + "where " +
									SERCH_BY_STRING +
									ORDER_BY);
					ps.setString(1, "%" + search + "%");
					ps.setInt(2, limit);
					ps.setInt(3, offset);
					return ps;
				},
				map()
		);
	}

	@Override
	public List<Post> searchByTags(int tagsCount, List<String> tags, int limit, int offset) {
		return jdbcTemplate.query(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(
							SELECT_FROM_POST + "where " +
									SEARCH_BY_TAGS +
									ORDER_BY);
					ps.setArray(1, connection.createArrayOf("varchar", tags.toArray()));
					ps.setInt(2, tagsCount);
					ps.setInt(3, limit);
					ps.setInt(4, offset);
					return ps;
				},
				map()
		);
	}

	@Override
	public Optional<Post> findById(Long id) {
		return Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(
				"select * from posts where id = " + id,
				map()
		)));
	}

	private RowMapper<Post> map() {
		return (rs, rowNum) -> new Post(
				rs.getLong("id"),
				rs.getString("title"),
				rs.getString("text"),
				jdbcTemplate.query(
						"select tagname from tags where postid = " + rs.getLong("id"),
						(resultSet, rowNumber) -> resultSet.getString("tagName")
				),
				rs.getLong("likescount"),
				rs.getInt("commentscount")
		);
	}

	@Override
	@Transactional
	public Long create(PostCreateDTO postDTO) {
		// Используем keyHolder для получения уникального идентификаотра записи с помощью returning в sql-запросе
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// Формируем insert-запрос для создания поста
		String sql = "insert into posts(title, text, likescount, commentscount) values(?, ?, ?, ?)";
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
					ps.setString(1, postDTO.getTitle());
					ps.setString(2, postDTO.getText());
					ps.setLong(3, 0);
					ps.setInt(4, 0);
					return ps;
				},
				keyHolder
		);

		// Формируем insert-запросы для создания тегов
		long postUID = Objects.requireNonNull(keyHolder.getKey()).longValue();
		creatTags(postDTO.getTags(), postUID);

		return postUID;
	}

	/**
	 * Метод обновляет пост по уникальному идентификатору из переданного представления. Записи тегов при обновлении
	 * пересоздаются (происходит удаление существующих тегов по идентификатору поста и повторное создание)
	 * @param postDTO переданное представление, содержащее поля для обновления поста и тегов
	 * @return обновленная запись поста
	 */
	@Override
	@Transactional
	public Optional<Post> update(PostUpdateDTO postDTO) {
		String updatePostSQL = "update posts set title = ?, text = ? where id = ?";
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(updatePostSQL, new String[]{"id"});
					ps.setString(1, postDTO.getTitle());
					ps.setString(2, postDTO.getText());
					ps.setLong(3, postDTO.getId());
					return ps;
				}
		);
		creatTags(postDTO.getTags(), postDTO.getId());

		return findById(postDTO.getId());
	}

	private void creatTags(List<String> tags, Long postId) {
		jdbcTemplate.update("delete from tags where postid = ?", postId);
		jdbcTemplate.batchUpdate(
				"insert into tags(postid, tagname) values(?, ?)",
				tags,
				tags.size(),
				(ps, tag) -> {
					ps.setLong(1, postId);
					ps.setString(2, tag);
				}
		);
	}

	/**
	 * Метод удаляет пост из базы данных по уникальному идентификатору. Записи из таблиц хранящих теги и комментарии
	 * будут удалены автоматически, так как DDL схемы таблиц содержат опцию on delete cascade по ключу postId
	 * @param id Уникальный идентификтор поста
	 */
	@Override
	public void delete(Long id) {
		jdbcTemplate.update("delete from posts where id = ?", id);
	}

	@Override
	public long incrementLikes(Long id) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "update posts set likescount = likescount + 1 where id = ?";
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(sql, new String[]{"likescount"});
					ps.setLong(1, id);
					return ps;
				}, keyHolder
		);

		return Optional.ofNullable(keyHolder.getKey())
				.map(Number::longValue)
				.orElseThrow(() -> new RuntimeException("Likes count increment incorrect."));
	}
}
