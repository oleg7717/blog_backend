package ru.goncharenko.blog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {
	@NotBlank(message = "Title cannot be blank")
	@NotNull(message = "Title cannot be null")
	private String title;
	@NotBlank(message = "Text cannot be null")
	private String text;
	@NotNull(message = "Tags list cannot be null")
	private List<String> tags;
}
