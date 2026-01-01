package ru.goncharenko.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse extends ApiResponse {
	private String message;
	private int statusCode;

	public static ApiErrorResponse error(String message, int statusCode) {
		return ApiErrorResponse.builder()
				.message(message)
				.statusCode(statusCode)
				.build();
	}
}
