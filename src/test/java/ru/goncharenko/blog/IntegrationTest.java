package ru.goncharenko.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class IntegrationTest extends JDBCTest {
	@Autowired
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
}
