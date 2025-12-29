package ru.goncharenko.blog.post.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
	@ResponseBody
	public List<Post> getPosts() {
		return service.getAllPosts();
	}

}
