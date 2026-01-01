package ru.goncharenko.blog.post.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.goncharenko.blog.post.dto.PostListResponse;
import ru.goncharenko.blog.post.dto.SinglePostResponse;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService service;

	public PostController(PostService service) {
		this.service = service;
	}

	@GetMapping
	public PostListResponse index(@RequestParam(required = false, name = "search") String search,
	                              @RequestParam(name = "pageSize") int pageSize,
	                              @RequestParam(name = "pageNumber") int pageNumber) {
		return service.getPosts(search, pageSize, pageNumber);
	}

	@GetMapping(path = "/{id}")
	public SinglePostResponse show(@PathVariable(name = "id") long id) {
		return service.getPostById(id);
	}

	@PostMapping()
	public SinglePostResponse newPost(@RequestBody PostCreateDTO postDTO) {
		return service.newPost(postDTO);
	}

	@PutMapping(path = "/{id}")
	public SinglePostResponse update(@PathVariable(name = "id") long id, @RequestBody PostUpdateDTO postDTO) {
		//ToDo нельзя менять UID записи
		return service.update(id, postDTO);
	}
}
