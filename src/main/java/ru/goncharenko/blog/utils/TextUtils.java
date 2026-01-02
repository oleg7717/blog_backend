package ru.goncharenko.blog.utils;

import ru.goncharenko.blog.post.model.Post;

import java.util.List;

public class TextUtils {
	private static final int SUBSTRING_TO = 128;

	public static void getSubstringPosts(List<Post> posts) {
		posts.stream()
				.filter(post -> post.getText().length() > 128)
				.forEach(post -> post.setText(post.getText().substring(0, SUBSTRING_TO) + "..."));
	}
}
