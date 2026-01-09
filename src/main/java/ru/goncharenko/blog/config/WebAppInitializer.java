package ru.goncharenko.blog.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{BlogAppConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{WebConfig.class};
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		long maxFileSize = 5 * 1024 * 1024; // 5 MB
		long maxRequestSize = 20 * 1024 * 1024; // 25 MB
		int fileSizeThreshold = 1024 * 1024; // 1 MB
		String location = ""; // Use system default

		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
				location,
				maxFileSize,
				maxRequestSize,
				fileSizeThreshold
		);

		registration.setMultipartConfig(multipartConfigElement);
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected String getServletName() {
		return "dispatcher";
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
}
