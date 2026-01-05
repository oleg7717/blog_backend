package ru.goncharenko.blog.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.goncharenko.blog.dto.BaseDTO;

import java.util.List;

@Setter
@Getter
public class PostUpdateDTO implements BaseDTO {
	@NotNull(message = "Id cannot be null")
	private long id;
	@NotNull(message = "Title cannot be null")
	private String title;
	@NotNull(message = "Text cannot be null")
	private String text;
	@NotNull(message = "Tags list cannot be null")
	private List<String> tags;
}
