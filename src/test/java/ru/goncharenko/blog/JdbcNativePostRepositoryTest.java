package ru.goncharenko.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.goncharenko.blog.config.BlogAppConfig;
import ru.goncharenko.blog.config.WebConfig;
import ru.goncharenko.blog.post.dto.PostCreateDTO;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {
		BlogAppConfig.class,
		WebConfig.class
})
public class JdbcNativePostRepositoryTest extends IntegrationTest {
	@Autowired
	private PostRepository repository;

	@Test
	void save_shouldAddPostToDatabase() {
		PostCreateDTO post = new PostCreateDTO("Пост про космос", "Текст поста про космос", new ArrayList<>(Arrays.asList("cosmos", "science")));

		repository.create(post);

		List<Post> all = repository.getRecords(10, 0);
		Post saved = all.stream().filter(u -> u.getId() == 4L).findFirst().orElse(null);

		assertNotNull(saved);
		assertEquals("Пост про космос", saved.getTitle());
		assertEquals(2, saved.getTags().size());
	}

	@Test
	void findAll_shouldReturnAllUsers() {
		List<Post> all = repository.getRecords(10, 0);

		assertNotNull(all);
		assertEquals(3, all.size());
		Post first = all.getFirst();
		assertEquals(1L, first.getId());
		assertEquals("Пост про спорт", first.getTitle());
	}

	@Test
	void deleteById_shouldRemoveUserFromDatabase() {
		repository.delete(1L);

		List<Post> all = repository.getRecords(10, 0);
		assertEquals(2, all.size());
		assertTrue(all.stream().noneMatch(u -> u.getId() == 1L));
	}
}
