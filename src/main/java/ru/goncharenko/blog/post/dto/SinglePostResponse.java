package ru.goncharenko.blog.post.dto;

import lombok.Getter;
import lombok.Setter;
import ru.goncharenko.blog.dto.ApiResponse;

import java.util.List;

@Setter
@Getter
public class SinglePostResponse implements ApiResponse {
	private Long id;
	private String title;
	private String text;
	private List<String> tags;
	private long likesCount;
	private int commentsCount;
}
