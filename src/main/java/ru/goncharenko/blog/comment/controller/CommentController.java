package ru.goncharenko.blog.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.goncharenko.blog.comment.dto.CommentCreateDTO;
import ru.goncharenko.blog.comment.dto.CommentUpdateDTO;
import ru.goncharenko.blog.comment.dto.SingleCommentResponse;
import ru.goncharenko.blog.comment.model.Comment;
import ru.goncharenko.blog.comment.service.CommentService;
import ru.goncharenko.blog.dto.BaseDTO;
import ru.goncharenko.blog.exception.ValidationException;
import ru.goncharenko.blog.utils.ValidationUtils;

import java.util.List;

@RestController
@RequestMapping("/posts/{postid}/comments")
public class CommentController {
	private final CommentService service;
	private final ValidationUtils<BaseDTO> validationUtils;

	public CommentController(CommentService service, ValidationUtils<BaseDTO> validationUtils) {
		this.service = service;
		this.validationUtils = validationUtils;
	}

	@GetMapping
	public List<Comment> index(@PathVariable("postid") Long postId) {
		return service.getCommentsByPostId(postId);
	}

	@GetMapping(path = "/{id}")
	public SingleCommentResponse show(@PathVariable("postid") Long postId, @PathVariable("id") long id) {
		return service.getComment(id, postId);
	}

	@PostMapping()
	public SingleCommentResponse newComment(@PathVariable("postid") Long postId,
	                                        @RequestBody CommentCreateDTO commentDTO) {
		validationUtils.validateDTO(commentDTO);
		if (postId != commentDTO.getPostId()) {
			throw new ValidationException("The post ID in the URL must match the post ID in the request body.");
		}


		return service.newComment(commentDTO);
	}

	@PutMapping(path = "/{id}")
	public SingleCommentResponse update(@PathVariable("postid") Long postId,
	                                    @PathVariable("id") long id,
	                                    @RequestBody CommentUpdateDTO commentDTO) {
		validationUtils.validateDTO(commentDTO);
		if (id != commentDTO.getId()) {
			throw new ValidationException("The comment ID in the URL must match the comment ID in the request body.");
		} else if (postId != commentDTO.getPostId()) {
			throw new ValidationException("The post ID in the URL must match the post ID in the request body.");
		}


		return service.update(commentDTO);
	}

	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") long id) {
		service.delete(id);
	}
}
