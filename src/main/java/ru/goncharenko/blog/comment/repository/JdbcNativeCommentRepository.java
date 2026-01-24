package ru.goncharenko.blog.comment.repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.goncharenko.blog.comment.dto.CommentCreateDTO;
import ru.goncharenko.blog.comment.dto.CommentUpdateDTO;
import ru.goncharenko.blog.comment.model.Comment;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {
	private final JdbcTemplate jdbcTemplate;

	public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Comment> findByPostId(Long postId) {
		return jdbcTemplate.query(
				"select * from comments where postid = " + postId,
				map()
		);
	}

	public Optional<Comment> findByIdAndPostId(Long id, Long postId) {
		return Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(
				"select * from comments where id = " + id + " and postid = " + postId,
				map()
		)));
	}

	private RowMapper<Comment> map() {
		return (rs, rowNum) -> new Comment(
				rs.getLong("id"),
				rs.getString("text"),
				rs.getLong("postid")
		);
	}

	/**
	 * Метод создает комментарий в базе данных по переданному представлению
	 * @param commentDTO представление комментария для создания
	 * @return уникальный идентификатор комментария
	 */
	@Override
	@Transactional
	public Long create(CommentCreateDTO commentDTO) {
		// Используем keyHolder для получения уникального идентификаотра записи
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// Формируем insert-запрос для создания поста
		String sql = "insert into comments(text, postid) values(?, ?)";
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
					ps.setString(1, commentDTO.getText());
					ps.setLong(2, commentDTO.getPostId());
					return ps;
				},
				keyHolder
		);
		jdbcTemplate.update("update posts set commentscount = commentscount + 1 WHERE id = ?",
				commentDTO.getPostId()
		);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public Optional<Comment> update(CommentUpdateDTO commentDTO) {
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement("update comments set text = ? where id = ? and postid = ?", new String[]{"id"});
					ps.setString(1, commentDTO.getText());
					ps.setLong(2, commentDTO.getId());
					ps.setLong(3, commentDTO.getPostId());
					return ps;
				}
		);

		return findByIdAndPostId(commentDTO.getId(), commentDTO.getPostId());
	}

	/**
	 * Метод удаляет комментарий из базы данных по уникальному идентификатору
	 * @param id Уникальный идентификтор комментария
	 */
	@Override
	@Transactional
	public void delete(Long id) {
		Long postId = DataAccessUtils.singleResult(jdbcTemplate.query(
				"select postid from comments where id = " + id,
				(rs, rowNum) -> rs.getLong("postid")
		));

		String sql = "delete from comments where id = ?";
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(sql);
					ps.setLong(1, id);
					return ps;
				}
		);

		jdbcTemplate.update("update posts set commentsCount = commentscount - 1 WHERE id = ?", postId);
	}
}
