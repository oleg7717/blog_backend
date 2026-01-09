package ru.goncharenko.blog.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiMessageResponse implements ApiResponse {
	private String message;
	private int statusCode;

	public static ApiMessageResponse error(String message, int statusCode) {
		return ApiMessageResponse.builder()
				.message(message)
				.statusCode(statusCode)
				.build();
	}

	public static ApiMessageResponse success(String message) {
		return ApiMessageResponse.builder()
				.message(message)
				.statusCode(200)
				.build();
	}
}
