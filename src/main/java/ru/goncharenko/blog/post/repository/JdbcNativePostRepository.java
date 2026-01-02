package ru.goncharenko.blog.post.repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
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

	public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public long recordsCount() {
		Long count = jdbcTemplate.queryForObject("select count(id) from posts", Long.class);
		return count != null ? count : 0;
	}

	@Override
	public List<Post> getRecords(String search, int limit, int offset) {
		return jdbcTemplate.query(
				"select id, title, text, likescount, commentscount from posts limit " + limit + " offset " + offset,
				map()
		);
	}

	@Override
	public Optional<Post> findById(Long id) {
		return Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(
				"select id, title, text, likescount, commentscount from posts where id = " + id,
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
	public Optional<Post> create(PostCreateDTO postDTO) {
		// Используем keyHolder для получения уникального идентификаотра записи
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
				keyHolder);

		// Формируем insert-запросы для создания тегов
		long recordUID = Objects.requireNonNull(keyHolder.getKey()).longValue();
		creatTags(postDTO.getTags(), recordUID);

		return findById(recordUID);
	}

	@Override
	public Optional<Post> update(PostUpdateDTO postDTO) {
		jdbcTemplate.update("update posts set title = ?, text = ? where id = ?",
				postDTO.getTitle(),
				postDTO.getText(),
				postDTO.getId()
		);
		// ToDo Нужно обновлять теги, а не только добавлять
		creatTags(postDTO.getTags(), postDTO.getId());

		return findById(postDTO.getId());
	}

	private void creatTags(List<String> tags, long recordUID) {
		tags.forEach(tag -> {
			jdbcTemplate.update("insert into tags(postid, tagName) values(?, ?) " +
							"on conflict (postid, tagName) do nothing",
					recordUID,
					tag);
		});
	}

	@Override
	public void delete(Long id) {
		jdbcTemplate.update("delete from posts where id = ?", id);
	}

	@Override
	public long incrementLikes(long id) {
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
				.orElseThrow(() -> new RuntimeException("Likes count incorrect."));
	}
}
