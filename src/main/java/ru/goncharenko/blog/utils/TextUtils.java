package ru.goncharenko.blog.utils;

import ru.goncharenko.blog.post.model.Post;

import java.util.List;
import java.util.TreeMap;

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
				if (!searchWord.startsWith("#") && searchWord.length() > 1) {
					searchSubstring.append(searchWord).append(" ");
				}
			}
		}

		return searchSubstring.toString();
	}

	public static TreeMap<Integer, String> getTags(String search) {
		TreeMap<Integer, String> tags = new TreeMap<>();
		if (search != null) {
			String[] serchArr = search.split(" ");
			StringBuilder tagsString = new StringBuilder();
			int tagsCount = 0;
			for (String searchWord : serchArr) {
				if (searchWord.startsWith("#") && searchWord.length() > 1) {
					tagsString.append("'");
					tagsString.append(searchWord.substring(1));
					tagsString.append("', ");
					tagsCount++;
				}
			}

			if (!tagsString.isEmpty()) {
				tags.put(tagsCount, tagsString.substring(0, tagsString.length() - 2));
			}

		}

		return tags;
	}
}
