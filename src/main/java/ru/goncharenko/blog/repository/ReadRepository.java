package ru.goncharenko.blog.repository;

import java.util.List;
import java.util.Optional;

public interface ReadRepository<T, ID> {
	List<T> getRecords(String search, int limit, int offset);

	Optional<T> findById(ID id);
}
