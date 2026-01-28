package ru.goncharenko.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.goncharenko.blog.post.controller.PostController;
import ru.goncharenko.blog.post.mapper.PostMapper;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.post.repository.JdbcNativePostRepository;
import ru.goncharenko.blog.post.service.FilesService;
import ru.goncharenko.blog.post.service.PostService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PostController.class,
		includeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = {PostService.class, PostMapper.class, FilesService.class}
		))
@AutoConfigureMockMvc
public class PostControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private JdbcNativePostRepository repository;

	@Captor
	private ArgumentCaptor<String> searchCaptor;

	@Captor
	private ArgumentCaptor<Integer> tagsCountCaptor;

	@Captor
	private ArgumentCaptor<List<String>> tagsListCaptor;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void searchStringParse() throws Exception {
		when(repository.countByTagsAndSubstring(anyString(), anyInt(), anyList()))
				.thenReturn(1L);
		when(repository.searchByTagsAndSubstring(anyString(), anyInt(), anyList(), anyInt(), anyInt()))
				.thenReturn(List.of(new Post(
						1L,
						"Пост про спорт",
						"Нет ничего проще...",
						List.of("sport", "news"),
						0,
						1))
				);

		// Проверяем ответ контроллера на поиск по подстроке и тегу
		mockMvc.perform(get("/api/posts")
						.param("search", "Пост #sport")
						.param("pageNumber", "1")
						.param("pageSize", "2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.hasNext").value("false"))
				.andExpect(jsonPath("$.posts[0].tags", hasSize(2)))
				.andExpect(jsonPath("$.posts[0].title").value("Пост про спорт"));

		// Проверяем вызов метода посдчета количества постов
		verify(repository, times(1)).countByTagsAndSubstring(anyString(), anyInt(), anyList());

		// Проверяем вызов метода поиска постов с выбранныим параметрами
		verify(repository).searchByTagsAndSubstring(searchCaptor.capture(), tagsCountCaptor.capture(),
				tagsListCaptor.capture(), anyInt(), anyInt());
		assertEquals("Пост", searchCaptor.getValue());
		assertEquals(1, tagsCountCaptor.getValue());
		assertEquals(List.of("sport"), tagsListCaptor.getValue());
	}
}
