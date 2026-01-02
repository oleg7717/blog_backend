package ru.goncharenko.blog.post.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.goncharenko.blog.post.dto.LikeCountDTO;
import ru.goncharenko.blog.post.dto.PostListResponse;
import ru.goncharenko.blog.post.dto.SinglePostResponse;
import ru.goncharenko.blog.post.model.Post;
import ru.goncharenko.blog.utils.TextUtils;

import java.util.List;

@Mapper(
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PostMapper {
	SinglePostResponse postToSingleResponse(Post post);

	// Маппинг для списка постов со статусом
	@Mapping(target = "posts", source = "posts")
	@Mapping(target = "hasPrev", source = "hasPrev")
	@Mapping(target = "hasNext", source = "hasNext")
	@Mapping(target = "lastPage", source = "lastPage")
	PostListResponse<List<Post>> toListResponse(
			List<Post> posts,
			Boolean hasPrev,
			Boolean hasNext,
			Integer lastPage
	);

	@AfterMapping
	default void substringPosts(@MappingTarget PostListResponse<List<Post>> response) {
		if (response != null && response.getPosts() != null) {
			TextUtils.getSubstringPosts(response.getPosts());
		}
	}

	LikeCountDTO mapLikesCount(Long likesCount);
}
