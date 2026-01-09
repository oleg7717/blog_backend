package ru.goncharenko.blog.comment.service;

import org.springframework.stereotype.Service;
import ru.goncharenko.blog.comment.dto.CommentCreateDTO;
import ru.goncharenko.blog.comment.dto.CommentUpdateDTO;
import ru.goncharenko.blog.comment.dto.SingleCommentResponse;
import ru.goncharenko.blog.comment.mapper.CommentMapper;
import ru.goncharenko.blog.comment.model.Comment;
import ru.goncharenko.blog.comment.repository.CommentRepository;
import ru.goncharenko.blog.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CommentService {
	private final CommentRepository repository;
	private final CommentMapper mapper;

	public CommentService(CommentRepository repository, CommentMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public List<Comment> getCommentsByPostId(Long postId) {
		return repository.findByPostId(postId);
	}

	public SingleCommentResponse getComment(long id, long postId) {
		Comment comment = repository.findByIdAndPostId(id, postId)
				.orElseThrow(() -> new ResourceNotFoundException(exceptionMessage(id)));
		return mapper.map(comment);
	}

	public SingleCommentResponse newComment(CommentCreateDTO commentDTO) {
		Long uid = repository.create(commentDTO);
		SingleCommentResponse comment = mapper.mapDtoToComment(commentDTO);
		comment.setId(uid);
		return comment;
	}

	public SingleCommentResponse update(CommentUpdateDTO commentDTO) {
		Comment comment = repository.update(commentDTO)
				.orElseThrow(() -> new ResourceNotFoundException(exceptionMessage(commentDTO.getId())));
		return mapper.map(comment);
	}

	public void delete(long id) {
		repository.delete(id);
	}

	private String exceptionMessage(Long id) {
		return "Comment with id: " + id + " not found.";
	}
}
