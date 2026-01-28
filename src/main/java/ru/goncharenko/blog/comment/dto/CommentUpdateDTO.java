package ru.goncharenko.blog.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {
	@NotNull(message = "Id cannot be null")
	private long id;
	@NotNull(message = "Text cannot be null")
	private String text;
	@NotNull(message = "Post id cannot be null")
	private long postId;
}
