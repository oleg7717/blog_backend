package ru.goncharenko.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.goncharenko.blog.exception.ValidationException;
import ru.goncharenko.blog.response.ApiErrorResponse;
import ru.goncharenko.blog.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ApiErrorResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ApiErrorResponse handleValidationException(ValidationException ex) {
		return ApiErrorResponse.error(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiErrorResponse handleGenericException(Exception ex) {
		return ApiErrorResponse
				.error("An unexpected error occurred: " + ex.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
