package ru.goncharenko.blog.post.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.goncharenko.blog.post.model.Post;

import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {
	private final JdbcTemplate jdbcTemplate;

	public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Post> findAll() {
		return jdbcTemplate.query(
				"select id, title, text, likesCount, commentsCount from posts",
				(rs, rowNum) -> new Post(
						rs.getLong("id"),
						rs.getString("title"),
						rs.getString("text"),
						rs.getLong("likesCount"),
						rs.getInt("commentsCount")
				));
	}
}
