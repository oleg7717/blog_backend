package ru.goncharenko.blog.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.goncharenko.blog.dto.ApiResponse;
import ru.goncharenko.blog.post.model.Post;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class PostListResponse extends ApiResponse {
	private List<Post> posts;
}
