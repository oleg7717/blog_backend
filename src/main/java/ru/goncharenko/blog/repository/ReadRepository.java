package ru.goncharenko.blog.repository;

import java.util.Optional;

public interface ReadRepository<T, ID> {
	Optional<T> findById(ID id);
}
