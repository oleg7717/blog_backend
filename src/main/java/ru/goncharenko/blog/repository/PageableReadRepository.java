package ru.goncharenko.blog.repository;

import java.util.List;

public interface PageableReadRepository<T> {
	Long recordsCount();

	List<T> getRecords(int limit, int offset);

	List<T> searchByTagsAndSubstring(String search, int tagsCount, List<String> tags, int limit, int offset);

	List<T> searchBySubstring(String search, int limit, int offset);

	List<T> searchByTags(int tagsCount, List<String> tags, int limit, int offset);
}
