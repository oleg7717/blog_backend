package ru.goncharenko.blog.comment.repository;

import ru.goncharenko.blog.comment.dto.CommentCreateDTO;
import ru.goncharenko.blog.comment.dto.CommentUpdateDTO;
import ru.goncharenko.blog.comment.model.Comment;
import ru.goncharenko.blog.repository.CreateRepository;
import ru.goncharenko.blog.repository.DeleteRepository;
import ru.goncharenko.blog.repository.UpdateRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends
		CreateRepository<Long, CommentCreateDTO>,
		UpdateRepository<Comment, CommentUpdateDTO>,
		DeleteRepository<Long> {
	List<Comment> findByPostId(Long postId);

	Optional<Comment> findByIdAndPostId(Long id, Long postId);
}
