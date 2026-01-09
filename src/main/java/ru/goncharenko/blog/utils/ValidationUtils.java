package ru.goncharenko.blog.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import ru.goncharenko.blog.exception.ValidationException;

import java.util.Set;

@Component
public class ValidationUtils<T> {
	private final Validator validator;

	public ValidationUtils(Validator validator) {
		this.validator = validator;
	}

	public void validateDTO(T dto) {
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		if (!violations.isEmpty()) {
			StringBuilder textErrors = new StringBuilder();
			for (ConstraintViolation<T> violation : violations) {
				textErrors.append(violation.getMessage());
				textErrors.append(". ");
			}
			throw new ValidationException(textErrors.toString().trim());
		}
	}
}
