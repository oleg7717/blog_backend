package ru.goncharenko.blog.repository;

import java.util.Optional;

public interface UpdateRepository<T, ID, DTO> {
	Optional<T> update(ID id, DTO updateDTO);
}
