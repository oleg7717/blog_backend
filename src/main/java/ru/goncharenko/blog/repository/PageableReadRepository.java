package ru.goncharenko.blog.repository;

import java.util.List;

public interface PageableReadRepository<T> {
	Long recordsCount();

	List<T> getRecords(String search, int limit, int offset);
}
