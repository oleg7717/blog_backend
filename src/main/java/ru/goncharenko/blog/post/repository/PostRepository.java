package ru.goncharenko.blog.post.repository;

import ru.goncharenko.blog.post.model.Post;

import java.util.List;

public interface PostRepository {
	List<Post> findAll();
}
