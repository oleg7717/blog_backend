package ru.goncharenko.blog.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.goncharenko.blog.dto.BaseDTO;

@Setter
@Getter
public class CommentUpdateDTO implements BaseDTO {
	@NotNull(message = "Id cannot be null")
	private long id;
	@NotNull(message = "Text cannot be null")
	private String text;
	@NotNull(message = "Post id cannot be null")
	private long postId;
}
