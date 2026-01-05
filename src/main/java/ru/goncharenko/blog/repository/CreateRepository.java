package ru.goncharenko.blog.repository;

public interface CreateRepository<T, DTO> {
	T create(DTO createDTO);
}
