package ru.goncharenko.blog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiErrorResponse implements ApiResponse {
	private String message;
	private int statusCode;

	public static ApiErrorResponse error(String message, int statusCode) {
		return ApiErrorResponse.builder()
				.message(message)
				.statusCode(statusCode)
				.build();
	}
}
