package ru.goncharenko.blog.utils;

public class TextUtil {
	private static final int SUBSTRING_TO = 128;

	public static String getSubstringText(boolean substring, String text) {
		return text.length() > SUBSTRING_TO && substring ? text.substring(0, SUBSTRING_TO).trim() + "..." : text;
	}
}
