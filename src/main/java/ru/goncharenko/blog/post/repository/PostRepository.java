package ru.goncharenko.blog.post.repository;

import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.dto.PostUpdateDTO;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.repository.CreateRepository;
import ru.goncharenko.blog.repository.DeleteRepository;
import ru.goncharenko.blog.repository.ReadRepository;
import ru.goncharenko.blog.repository.UpdateRepository;

public interface PostRepository extends
		ReadRepository<Post, Long>,
		CreateRepository<Post, PostCreateDTO>,
		UpdateRepository<Post, Long, PostUpdateDTO>,
		DeleteRepository<Long> {
	long incrementLikes(long id);
}
