package ru.goncharenko.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.goncharenko.blog.dto.ApiErrorResponse;
import ru.goncharenko.blog.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private final String unexpectedErrorText = "An unexpected error occurred: ";

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ApiErrorResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiErrorResponse handleGenericException(Exception ex) {
		return ApiErrorResponse
				.error(unexpectedErrorText + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
