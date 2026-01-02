package ru.goncharenko.blog.repository;

import java.util.Optional;

public interface CreateRepository<T, DTO> {
	Optional<T> create(DTO createDTO);
}
