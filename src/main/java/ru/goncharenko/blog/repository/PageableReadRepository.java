package ru.goncharenko.blog.repository;

import java.util.List;

public interface PageableReadRepository<T> {
	List<T> getRecords(String search, int limit, int offset);
}
