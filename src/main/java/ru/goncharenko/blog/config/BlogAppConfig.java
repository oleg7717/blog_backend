package ru.goncharenko.blog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = {"ru.goncharenko.blog"},
		excludeFilters = {
				@ComponentScan.Filter(type = FilterType.ANNOTATION,
						value = EnableWebMvc.class)
		})
@PropertySource("classpath:application.properties")
public class BlogAppConfig {
}
