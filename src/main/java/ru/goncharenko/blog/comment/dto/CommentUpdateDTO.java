package ru.goncharenko.blog.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentUpdateDTO {
	private long id;
	private String text;
	private long postId;
}
