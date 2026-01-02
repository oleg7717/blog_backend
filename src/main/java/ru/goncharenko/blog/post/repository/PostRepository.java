package ru.goncharenko.blog.post.repository;

import ru.goncharenko.blog.post.dto.LikeCountDTO;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
	List<Post> getPosts(String search, int limit, int offset);

	Optional<Post> findPostById(long id);

	Optional<Post> newPost(PostCreateDTO postDTO);

	Optional<Post> update(long id, PostUpdateDTO postDTO);

	void delete(long id);

	long incrementLikes(long id);
}
