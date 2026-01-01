package ru.goncharenko.blog.post.service;

import org.springframework.stereotype.Service;
import ru.goncharenko.blog.post.dto.PostListResponse;
import ru.goncharenko.blog.exception.ResourceNotFoundException;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.dto.SinglePostResponse;
import ru.goncharenko.blog.post.mapper.PostMapper;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
	private final PostRepository repository;
	private final PostMapper mapper;

	public PostService(PostRepository repository, PostMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public PostListResponse getPosts(String search, int pageSize, int pageNumber) {
		List<Post> posts = repository.getPosts(search, pageSize, (pageNumber - 1) * pageSize);
		return mapper.toListResponse(posts, true,true,3);
		//ToDo добавить вывод количества страниц наличия следующей и предыдущей страниц
	}

	public SinglePostResponse getPostById(long id) {
		Post post = repository.findPostById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found."));
		return mapper.postToSingleResponse(post);
	}

	public SinglePostResponse newPost(PostCreateDTO postDTO) {
		// ToDo получать запись в сервисе после создания при помощи keyHolder или иначе
		Post post = repository.newPost(postDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found."));
		return mapper.postToSingleResponse(post);
	}

	public SinglePostResponse update(long id, PostUpdateDTO postDTO) {
		Post post = repository.update(id, postDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found."));
		return mapper.postToSingleResponse(post);
	}
}
