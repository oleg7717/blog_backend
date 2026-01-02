package ru.goncharenko.blog.post.dto;

import lombok.Getter;
import lombok.Setter;
import ru.goncharenko.blog.response.PageableApiResponse;

@Getter
@Setter
public class PostListResponse<T> extends PageableApiResponse {
	private T posts;
}
