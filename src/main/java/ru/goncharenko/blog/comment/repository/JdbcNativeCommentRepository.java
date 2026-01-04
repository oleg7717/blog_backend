package ru.goncharenko.blog.comment.repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
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
				"select id, text, postid from comments where postid = " + postId,
				map()
		);
	}

	public Optional<Comment> findByIdAndPostId(Long id, Long postId) {
		return Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(
				"select id, text, postid from comments where id = " + id + " and postid = " + postId,
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
	 * Метод создает комментарий в базе данных по переданному представлению. При добавлении срабатывает правило базы
	 * данных: comment_insert_rule, которое увеличивает счетчик количества комментариев в таблице posts
	 * @param commentDTO представление комментария для создания
	 * @return уникальный идентификатор комментария
	 */
	@Override
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

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public Optional<Comment> update(CommentUpdateDTO commentDTO) {
		jdbcTemplate.update("update comments set text = ? where id = ? and postid = ?",
				commentDTO.getText(),
				commentDTO.getId(),
				commentDTO.getPostId()
		);

		return findByIdAndPostId(commentDTO.getId(), commentDTO.getPostId());
	}

	/**
	 * Метод удаляет комментарий из базы данных по уникальному идентификатору. При удалении срабатывает правило базы
	 * данных: comment_delete_rule, которое уменьшает счетчик количества комментариев в таблице posts
	 * @param id Уникальный идентификтор комментария
	 */
	@Override
	public void delete(Long id) {
		jdbcTemplate.update("delete from comments where id = ?", id);
	}
}
