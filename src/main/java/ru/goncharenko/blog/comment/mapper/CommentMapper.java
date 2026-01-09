package ru.goncharenko.blog.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.goncharenko.blog.comment.dto.CommentCreateDTO;
import ru.goncharenko.blog.comment.dto.SingleCommentResponse;
import ru.goncharenko.blog.comment.model.Comment;

@Mapper(
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommentMapper {
	SingleCommentResponse map(Comment comment);

	SingleCommentResponse mapDtoToComment(CommentCreateDTO comment);
}
