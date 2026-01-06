package ru.goncharenko.blog;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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
public class CommentControllerIntegrationTest extends IntegrationTest {
	@Test
	void getCommentsByPostId() throws Exception {
		mockMvc.perform(get("/api/posts/3/comments"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].text").value("Первый комментарий к посту 3"));
	}

	@Test
	void getCommentById() throws Exception {
		mockMvc.perform(get("/api/posts/3/comments/2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.text").value("Первый комментарий к посту 3"));
	}

	@Test
	void createComment_andCheckPersists() throws Exception {
		String json = """
				    {
				        "text": "Комментарий к посту 2",
				        "postId": 2
				    }
				""";
		mockMvc.perform(post("/api/posts/2/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(4))
				.andExpect(jsonPath("$.text").value("Комментарий к посту 2"));

		mockMvc.perform(get("/api/posts/2/comments")
						.param("pageNumber", "1")
						.param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void updateComment() throws Exception {
		String json = """
		      		{
		      			"id": 1,
	      				"text": "Первый комментарий к посту 1",
	      				"postId": 1
		      		}
				""";

		mockMvc.perform(put("/api/posts/1/comments/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/posts/1/comments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$.[0].text").value("Первый комментарий к посту 1"));
	}
}
