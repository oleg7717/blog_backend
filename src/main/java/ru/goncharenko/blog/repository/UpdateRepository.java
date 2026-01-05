package ru.goncharenko.blog.repository;

import java.util.Optional;

public interface UpdateRepository<T, DTO> {
	Optional<T> update(DTO updateDTO);
}
