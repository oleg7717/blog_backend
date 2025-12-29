package ru.goncharenko.blog.post.service;

import org.springframework.stereotype.Service;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
	private final PostRepository repository;

	public PostService(PostRepository repository) {
		this.repository = repository;
	}

	public List<Post> getAllPosts() {
		return repository.findAll();
	}
}
