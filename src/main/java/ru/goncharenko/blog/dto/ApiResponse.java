package ru.goncharenko.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
	private Boolean hasPrev;
	private Boolean hasNext;
	private Integer lastPage;
}
