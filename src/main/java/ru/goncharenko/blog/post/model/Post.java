package ru.goncharenko.blog.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	private long id;
	private String title;
	private String text;
//	private List<Tag> tags;
	private long likesCount;
	private int commentsCount;
}
