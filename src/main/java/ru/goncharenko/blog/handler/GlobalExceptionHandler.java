package ru.goncharenko.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.goncharenko.blog.response.ApiMessageResponse;
import ru.goncharenko.blog.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiMessageResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ApiMessageResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiMessageResponse handleGenericException(Exception ex) {
		return ApiMessageResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiMessageResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder textErrors = new StringBuilder();
		ex.getBindingResult().getAllErrors()
				.forEach(error -> textErrors.append(error.getDefaultMessage()).append(". "));

		return ApiMessageResponse
				.error(String.join(". ", textErrors.toString().trim()), HttpStatus.BAD_REQUEST.value());
	}
}
