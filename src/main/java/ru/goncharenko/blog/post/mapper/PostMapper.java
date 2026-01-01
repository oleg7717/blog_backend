package ru.goncharenko.blog.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.goncharenko.blog.post.dto.PostListResponse;
import ru.goncharenko.blog.post.dto.SinglePostResponse;
import ru.goncharenko.blog.post.model.Post;

import java.util.List;

@Mapper(
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostMapper {
	public abstract SinglePostResponse postToSingleResponse(Post post);

	// Маппинг для списка постов со статусом
	// ToDo усечение до 128 символов на уровне маппера
	@Mapping(target = "posts", source = "posts")
	@Mapping(target = "hasPrev", source = "hasPrev")
	@Mapping(target = "hasNext", source = "hasNext")
	@Mapping(target = "lastPage", source = "lastPage")
	public abstract PostListResponse toListResponse(
			List<Post> posts,
			Boolean hasPrev,
			Boolean hasNext,
			Integer lastPage
	);
}
