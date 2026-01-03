package ru.goncharenko.blog.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.goncharenko.blog.exception.ValidationException;
import ru.goncharenko.blog.post.dto.LikeCountDTO;
import ru.goncharenko.blog.post.dto.PostListResponse;
import ru.goncharenko.blog.post.dto.SinglePostResponse;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService service;

	public PostController(PostService service) {
		this.service = service;
	}

	@GetMapping
	public PostListResponse<List<Post>> index(@RequestParam(required = false, name = "search") String search,
	                                          @RequestParam(name = "pageSize") int pageSize,
	                                          @RequestParam(name = "pageNumber") int pageNumber) {
		return service.getPosts(search, pageSize, pageNumber);
	}

	@GetMapping(path = "/{id}")
	public SinglePostResponse show(@PathVariable("id") long id) {
		return service.getPostById(id);
	}

	@PostMapping()
	public SinglePostResponse newPost(@RequestBody PostCreateDTO postDTO) {
		//ToDo проверка полей dto
		return service.newPost(postDTO);
	}

	@PutMapping(path = "/{id}")
	public SinglePostResponse update(@PathVariable("id") long id, @RequestBody PostUpdateDTO postDTO) {
		//ToDo проверка полей dto
		if (id != postDTO.getId()) {
			throw new ValidationException("The post ID in the URL must match the post ID in the request body.");
		}

		return service.update(postDTO);
	}

	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") long id) {
		service.delete(id);
	}

	@PostMapping(path = "/{id}/likes")
	public LikeCountDTO incrementLikes(@PathVariable("id") long id) {
		return service.incrementLikes(id);
	}

	//ToDo загрузка вложений
}
