package ru.goncharenko.blog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.goncharenko.blog.dto.AbstractDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements AbstractDTO {
	private Long id;
	private String title;
	private String text;
	private List<String> tags;
	private long likesCount;
	private int commentsCount;
}
