package ru.goncharenko.blog;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")

public abstract class IntegrationTest {
	@Autowired
	WebApplicationContext wac;
	@Autowired
	JdbcTemplate jdbcTemplate;

	MockMvc mockMvc;

	/*	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("postgres")
			.withUsername("postgres")
			.withPassword("password");
	static {
		postgresContainer.start();
	}

	@DynamicPropertySource
	static void registerDynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
	}*/

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		jdbcTemplate.execute("delete from comments;");
		jdbcTemplate.execute("delete from tags;");
		jdbcTemplate.execute("delete from posts;");
		jdbcTemplate.execute("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;");
		jdbcTemplate.execute("ALTER TABLE posts ALTER COLUMN id RESTART WITH 1;");

		jdbcTemplate.execute("insert into posts(title, text, likesCount, commentsCount) values ('Пост про спорт'," +
				"'Нет ничего проще, чем составить символическую сборную лучших баскетболистов XXI века в рамках " +
				"подведения итогов первых 25 лет. И именно поэтому одновременно нет и ничего сложнее:', " +
				"0, 1);");
		jdbcTemplate.execute("insert into posts(title, text, likesCount, commentsCount) values ('Пост про финансы', 'Текст поста', 0, 0);");
		jdbcTemplate.execute("insert into posts(title, text, likesCount, commentsCount) values ('Пост про политику', 'Текст поста', 0, 2);");

		jdbcTemplate.execute("insert into tags(postid, tagname) values (1, 'sport');");
		jdbcTemplate.execute("insert into tags(postid, tagname) values (2, 'finance');");
		jdbcTemplate.execute("insert into tags(postid, tagname) values (2, 'politic');");
		jdbcTemplate.execute("insert into tags(postid, tagname) values (3, 'sport');");
		jdbcTemplate.execute("insert into tags(postid, tagname) values (3, 'politic');");

		jdbcTemplate.execute("insert into comments(postid, text) values (1, 'Комментарий к посту 1');");
		jdbcTemplate.execute("insert into comments(postid, text) values (3, 'Первый комментарий к посту 3');");
		jdbcTemplate.execute("insert into comments(postid, text) values (3, 'Второй комментарий к посту 3');");
	}
}
