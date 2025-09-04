package com.melaniia.blog.mappers;

import com.melaniia.blog.domain.CreatePostRequest;
import com.melaniia.blog.domain.UpdatePostRequest;
import com.melaniia.blog.domain.dtos.CreatePostRequestDto;
import com.melaniia.blog.domain.dtos.PostDto;
import com.melaniia.blog.domain.dtos.UpdatePostRequestDto;
import com.melaniia.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    public PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
