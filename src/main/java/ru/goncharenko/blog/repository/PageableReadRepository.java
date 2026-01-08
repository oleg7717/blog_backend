package ru.goncharenko.blog.repository;

import java.util.List;

public interface PageableReadRepository<T> {
	Long countAllRecords();

	Long countByTagsAndSubstring(String search, int tagsCount, List<String> tags);

	Long countBySubstring(String search);

	Long countByTags(int tagsCount, List<String> tags);

	List<T> getRecords(int limit, int offset);

	List<T> searchByTagsAndSubstring(String search, int tagsCount, List<String> tags, int limit, int offset);

	List<T> searchBySubstring(String search, int limit, int offset);

	List<T> searchByTags(int tagsCount, List<String> tags, int limit, int offset);
}
