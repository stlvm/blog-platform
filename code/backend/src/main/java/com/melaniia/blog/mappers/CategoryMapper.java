package com.melaniia.blog.mappers;

import com.melaniia.blog.domain.PostStatus;
import com.melaniia.blog.domain.dtos.CategoryDto;
import com.melaniia.blog.domain.dtos.CreateCategoryRequest;
import com.melaniia.blog.domain.entities.Category;
import com.melaniia.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper{
    @Mapping(target = "postCount", source="posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto (Category category);
    Category toEntity(CreateCategoryRequest createCategoryRequest);
    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .count();
    }
}
