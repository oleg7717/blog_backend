package ru.goncharenko.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractPageResponse {
	private List<? extends AbstractDTO> posts;
	private boolean hasPrev;
	private boolean hasNext;
	private int lastPage;
}
