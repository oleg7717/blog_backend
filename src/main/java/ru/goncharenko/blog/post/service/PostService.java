package ru.goncharenko.blog.post.service;

import org.springframework.stereotype.Service;
import ru.goncharenko.blog.dto.AbstractPageResponse;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostDTO;
import ru.goncharenko.blog.post.mapper.PostMapper;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
	private final PostRepository repository;
	private final PostMapper mapper;

	public PostService(PostRepository repository, PostMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public AbstractPageResponse getPosts(String search, int pageSize, int pageNumber) {
		List<PostDTO> posts = repository.getPosts(search, pageSize, pageNumber).stream().map(mapper::map).collect(Collectors.toList());
		return new AbstractPageResponse(
				posts,
//				postPage.hasPrevious(),
//				postPage.hasNext(),
//				postPage.getTotalPages() - 1
				true,true,3
				//ToDo добавить вывод количества страницб наличия следующей и предыдущей страниц
		);
	}

	public Post getPost(long id) {
		return repository.getPost(id);
	}

	public Post createPost(PostCreateDTO postDTO) {
		return repository.createPost(postDTO);
	}
}
