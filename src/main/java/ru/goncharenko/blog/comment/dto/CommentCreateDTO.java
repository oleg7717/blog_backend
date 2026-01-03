package ru.goncharenko.blog.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentCreateDTO {
	private String text;
	private long postId;
}
