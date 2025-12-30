package ru.goncharenko.blog.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PostCreateDTO {
	private String title;
	private String text;
	private List<String> tags;
}
