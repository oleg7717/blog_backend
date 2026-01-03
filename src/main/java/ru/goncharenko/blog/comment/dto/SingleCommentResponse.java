package ru.goncharenko.blog.comment.dto;

import lombok.Getter;
import lombok.Setter;
import ru.goncharenko.blog.response.ApiResponse;

@Setter
@Getter
public class SingleCommentResponse implements ApiResponse {
	private long id;
	private String text;
	private long postId;
}
