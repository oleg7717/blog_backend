package ru.goncharenko.blog.post.repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.model.Post;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

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
	public List<Post> getPosts(String search, int limit, int offset) {
		return jdbcTemplate.query(
				"select id, title, text, likesCount, commentsCount from posts limit " + limit + " offset " + offset,
				map(true)
		);
	}

	@Override
	public Post getPost(long id) {
		return DataAccessUtils.singleResult(jdbcTemplate.query(
				"select id, title, text, likesCount, commentsCount from posts where id = " + id,
				map(false)
		));
	}

	private RowMapper<Post> map(boolean substring) {
		return (rs, rowNum) -> new Post(
				rs.getLong("id"),
				rs.getString("title"),
				getSubstringText(substring, rs.getString("text")),
				jdbcTemplate.query(
						"select tagname from tags where postid = " + rs.getLong("id"),
						(resultSet, rowNumber) -> resultSet.getString("tagName")
				),
				rs.getLong("likesCount"),
				rs.getInt("commentsCount")
		);
	}

	@Override
	public Post createPost(PostCreateDTO postDTO) {
		// Используем keyHolder для получения уникального идентификаотра записи
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// Формируем insert-запрос для создания поста
		String sql = "insert into posts(title, text, likesCount, commentsCount) values(?, ?, ?, ?)";
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, postDTO.getTitle());
					ps.setString(2, postDTO.getText());
					ps.setLong(3, 0);
					ps.setInt(4, 0);
					return ps;
				},
				keyHolder);

		// Формируем insert-запросы для создания тегов
		long recordUID = (long) keyHolder.getKeys().get("id");
		postDTO.getTags().forEach(tag -> {
			jdbcTemplate.update("insert into tags(postid, tagName) values(?, ?)",
					recordUID, tag);
		});

		return getPost(recordUID);
	}

	private String getSubstringText(boolean substring, String text) {
		return text.length() > 128 && substring ? text.substring(0, 128).trim() + "..." : text;
	}
}
