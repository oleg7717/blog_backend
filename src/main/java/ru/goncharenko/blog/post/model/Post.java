package ru.goncharenko.blog.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	private long id;
	private String title;
	private String text;
	private List<String> tags;
	private long likesCount;
	private int commentsCount;
}
