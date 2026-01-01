package ru.goncharenko.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageableApiResponse implements ApiResponse {
	private Boolean hasPrev;
	private Boolean hasNext;
	private Integer lastPage;
}

