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
import ru.goncharenko.blog.utils.TextUtils;

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
		String searchString = TextUtils.getSearchString(search);
		List<String> tags = TextUtils.getTags(search);
		List<Post> posts;
		int offset = (pageNumber - 1) * pageSize;
		long count;
		if (!searchString.isEmpty() && !tags.isEmpty()) {
			posts = repository.searchByTagsAndSubstring(searchString, tags.size(), tags, pageSize, offset);
			count = repository.recordsCountByTagsAndSubstring(searchString, tags.size(), tags);
		} else if (!searchString.isEmpty()) {
			posts = repository.searchBySubstring(searchString, pageSize, offset);
			count = repository.recordsCountBySubstring(searchString);
		} else if (!tags.isEmpty()){
			posts = repository.searchByTags(tags.size(), tags, pageSize, offset);
			count = repository.recordsCountByTags(tags.size(), tags);
		} else {
			posts = repository.getRecords(pageSize, offset);
			count = repository.recordsCount();
		}

		int pages = (int) Math.ceilDiv(count, pageSize);
		boolean hasPrev = pages > 1 && pageNumber > 1;
		boolean hasNext = pages > 1 && pageNumber < pages;

		return mapper.toListResponse(posts, hasPrev,hasNext, pages);
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
