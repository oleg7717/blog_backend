package ru.goncharenko.blog.utils;

import ru.goncharenko.blog.post.model.Post;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {
	private static final int SUBSTRING_TO = 128;

	public static void getSubstringPosts(List<Post> posts) {
		posts.stream()
				.filter(post -> post.getText().length() > 128)
				.forEach(post -> post.setText(post.getText().substring(0, SUBSTRING_TO) + "..."));
	}

	public static String getSearchString(String search) {
		StringBuilder searchSubstring = new StringBuilder();
		if (search != null) {
			String[] serchArr = search.split(" ");
			for (String searchWord : serchArr) {
				if (!searchWord.startsWith("#")) {
					searchSubstring.append(searchWord).append(" ");
				}
			}
		}

		return searchSubstring.toString().trim();
	}

	public static List<String> getTags(String search) {
		List<String> tagsArray = new ArrayList<>();
		if (search != null) {
			String[] serchArr = search.split(" ");
			for (String searchWord : serchArr) {
				if (searchWord.startsWith("#") && searchWord.length() > 1) {
					tagsArray.add(searchWord.substring(1));
				}
			}
		}

		return tagsArray;
	}
}
