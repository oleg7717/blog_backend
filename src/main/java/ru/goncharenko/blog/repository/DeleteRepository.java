package ru.goncharenko.blog.repository;

public interface DeleteRepository<T> {
	void delete(T id);
}
