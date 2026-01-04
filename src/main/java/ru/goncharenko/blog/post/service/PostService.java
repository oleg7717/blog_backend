package ru.goncharenko.blog.post.service;

import org.springframework.stereotype.Service;
import ru.goncharenko.blog.post.dto.LikeCountDTO;
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

	public PostListResponse<List<Post>> getPosts(String search, int pageSize, int pageNumber) {
		List<Post> posts = repository.getRecords(search, pageSize, (pageNumber - 1) * pageSize);

		int pages = (int) Math.ceilDiv(repository.recordsCount(), pageSize);
		boolean hasPrev = pages > 1 && pageNumber > 1;
		boolean hasNext = pages > 1 && pageNumber < pages;

		return mapper.toListResponse(posts, hasPrev,hasNext, pages);
		//ToDo добавить поиск согласно правилам поиска
	}

	public SinglePostResponse getPostById(long id) {
		Post post = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found."));
		return mapper.postToSingleResponse(post);
	}

	public SinglePostResponse newPost(PostCreateDTO postDTO) {
		Long uid = repository.create(postDTO);
		SinglePostResponse post = mapper.postToSingleResponse(postDTO);
		post.setId(uid);
		return post;
	}

	public SinglePostResponse update(PostUpdateDTO postDTO) {
		Post post = repository.update(postDTO)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id: " + postDTO.getId() + " not found."));
		return mapper.postToSingleResponse(post);
	}

	public void delete(long id) {
		repository.delete(id);
	}

	public LikeCountDTO incrementLikes(long id) {
		return mapper.mapLikesCount(repository.incrementLikes(id));
	}
}
