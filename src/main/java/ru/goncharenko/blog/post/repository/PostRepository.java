package ru.goncharenko.blog.post.repository;

import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.model.Post;

import java.util.List;

public interface PostRepository {
	List<Post> getPosts(String search, int limit, int offset);

	Post getPost(long id);

	Post createPost(PostCreateDTO postDTO);
}
