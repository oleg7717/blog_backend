package ru.goncharenko.blog;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.goncharenko.blog.config.BlogAppConfig;
import ru.goncharenko.blog.config.WebConfig;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {
		BlogAppConfig.class,
		WebConfig.class
})
public class PostControllerIntegrationTest extends IntegrationTest {
	@Test
	void getPostsOnFirstPage() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("pageNumber", "1")
						.param("pageSize", "2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.posts", hasSize(2)))
				.andExpect(jsonPath("$.posts[1].tags", hasSize(2)));
	}

	@Test
	void getPostsOnNonExistPage() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("pageNumber", "10")
						.param("pageSize", "2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.posts", hasSize(0)));
	}

	@Test
	void getPostsOnIncorrectPage() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("pageNumber", "-1")
						.param("pageSize", "2"))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Page number can not be less then one."));
	}

	@Test
	void getPostsWithSearch() throws Exception {
		mockMvc.perform(get("/api/posts?pageNumber=1&pageSize=2")
						.param("search", "ро сп")
						.param("pageNumber", "1")
						.param("pageSize", "2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.posts[0].title").value("Пост про спорт"));
	}

	@Test
	void getPostsWithSearchByTags() throws Exception {
		mockMvc.perform(get("/api/posts?pageNumber=1&pageSize=10")
						.param("search", "Пост #sport")
						.param("pageNumber", "1")
						.param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.posts", hasSize(2)));
	}

	@Test
	void getAllPosts() throws Exception {
		mockMvc.perform(get("/api/posts?pageNumber=1&pageSize=10")
						.param("pageNumber", "1")
						.param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.posts", hasSize(3)))
				.andExpect(jsonPath("$.hasNext").value("false"));
	}

	@Test
	void getPost() throws Exception {
		mockMvc.perform(get("/api/posts/3"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.title").value("Пост про политику"))
				.andExpect(jsonPath("$.tags", hasSize(2)));
	}

	@Test
	void createPost_andCheckPersists() throws Exception {
		String json = """
				    {
				        "title": "Название поста 4",
						"text": "Текст поста в формате Markdown...",
				        "tags": ["news", "society"]
				    }
				""";

		mockMvc.perform(post("/api/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(4))
				.andExpect(jsonPath("$.title").value("Название поста 4"));

		mockMvc.perform(get("/api/posts?pageNumber=1&pageSize=10")
						.param("pageNumber", "1")
						.param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.posts", hasSize(4)));
	}

	@Test
	void deletePost() throws Exception {
		mockMvc.perform(delete("/api/posts/4"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/posts?pageNumber=1&pageSize=10")
						.param("pageNumber", "1")
						.param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.posts", hasSize(3)));
	}

	@Test
	void uploadAndDownloadImage_success() throws Exception {
		byte[] jpegStub = new byte[]{(byte) 137, 80, 78, 71};
		MockMultipartFile file = new MockMultipartFile("image",
				"postImage.jpg",
				"image/jpeg",
				jpegStub);

		mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/{id}/image", 2L).file(file))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("File upload successfully"));

		mockMvc.perform(get("/api/posts/{id}/image", 2L))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_JPEG))
				.andExpect(content().bytes(jpegStub));
	}

	@Test
	void uploadImage_emptyFile_badRequest() throws Exception {
		MockMultipartFile empty = new MockMultipartFile("image",
				"empty.png",
				"image/jpeg",
				new byte[0]
		);

		mockMvc.perform(multipart(HttpMethod.PUT,"/api/posts/{id}/image", 1L).file(empty))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
				.andExpect(jsonPath("$.message").value("Uploaded file is empty"));
	}

	@Test
	void uploadImage_postNotFound_404() throws Exception {
		MockMultipartFile file = new MockMultipartFile("image",
				"postImage.png",
				"image/png",
				new byte[]{1, 2, 3}
		);

		mockMvc.perform(multipart(HttpMethod.PUT,"/api/posts/{id}/image", 10L).file(file))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Post with id: 10 not found."));
	}

	@Test
	void getImage_postHasNoImage() throws Exception {
		mockMvc.perform(get("/api/posts/{id}/image", 3L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Image file for post not found"));
	}

	@Test
	void getImage_postNotFound() throws Exception {
		mockMvc.perform(get("/api/posts/{id}/image", 10L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Post with id: 10 not found."));
	}
}
