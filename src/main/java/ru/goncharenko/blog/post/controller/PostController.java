package ru.goncharenko.blog.post.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import ru.goncharenko.blog.dto.BaseDTO;
import ru.goncharenko.blog.exception.ValidationException;
import ru.goncharenko.blog.post.dto.LikeCountDTO;
import ru.goncharenko.blog.post.dto.PostListResponse;
import ru.goncharenko.blog.post.dto.SinglePostResponse;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.service.FilesService;
import ru.goncharenko.blog.post.service.PostService;
import ru.goncharenko.blog.response.ApiMessageResponse;
import ru.goncharenko.blog.utils.ValidationUtils;

import java.util.List;

@RestController
@RequestMapping(path = "/api/posts")
public class PostController {
	private final PostService service;
	private final FilesService filesService;
	private final ValidationUtils<BaseDTO> validationUtils;

	public PostController(PostService service, FilesService filesService, ValidationUtils<BaseDTO> validationUtils) {
		this.service = service;
		this.filesService = filesService;
		this.validationUtils = validationUtils;
	}

	@GetMapping(path = "")
	public PostListResponse<List<Post>> index(@RequestParam(required = false, name = "search") String search,
	                                          @RequestParam(name = "pageSize") int pageSize,
	                                          @RequestParam(name = "pageNumber") int pageNumber) {
		if (pageNumber < 1) {
			throw new ValidationException("Page number can not be less then one.");
		}

		return service.getPosts(search, pageSize, pageNumber);
	}

	@GetMapping(path = "/{id}")
	public SinglePostResponse show(@PathVariable("id") long id) {
		return service.getPostById(id);
	}

	@PostMapping(path = "")
	@ResponseStatus(HttpStatus.CREATED)
	public SinglePostResponse newPost(@RequestBody PostCreateDTO postDTO/*, BindingResult bindingResult*/) {
		validationUtils.validateDTO(postDTO);

		return service.newPost(postDTO);
	}

	@PutMapping(path = "/{id}")
	public SinglePostResponse update(@PathVariable("id") long id, @RequestBody PostUpdateDTO postDTO) {
		validationUtils.validateDTO(postDTO);
		if (id != postDTO.getId()) {
			throw new ValidationException("The post ID in the URL must match the post ID in the request body.");
		}

		return service.update(postDTO);
	}

	/**
	 * Метод удаляет пост по уникальному идентификатору. Записи из таблиц хранящих теги и комментарии
	 * будут удалены автоматически, так как DDL схемы таблиц содержат опцию on delete cascade по ключу postId
	 * @param id Уникальный идентификтор поста
	 */

	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") long id) {
		service.delete(id);
		filesService.delete(id);
	}

	@PostMapping(path = "/{id}/likes")
	public LikeCountDTO incrementLikes(@PathVariable("id") long id) {
		return service.incrementLikes(id);
	}

	@PutMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ApiMessageResponse uploadImage(@PathVariable("id") long id, @RequestParam("image") MultipartFile file) {
		// Ищем пост по идентификатору, если поста не существует, то будет возвращен json с ошибкой
		service.getPostById(id);
		if (file.isEmpty()) {
			throw new ValidationException("Uploaded file is empty");
		}
		filesService.upload(id, file);

		return ApiMessageResponse.success("File upload successfully");
	}

	@GetMapping(path = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public Resource downloadImage(@PathVariable("id") long id) {
		// Ищем пост по идентификатору, если поста не существует, то будет возвращен json с ошибкой
		service.getPostById(id);
		return filesService.download(id);
	}
}
