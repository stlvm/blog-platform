package com.melaniia.blog.domain.dtos;

import com.melaniia.blog.domain.PostStatus;
import com.melaniia.blog.domain.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title name must be between {min} and {max} characters")
    private String title;
    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 50000, message = "Content must be between {min} and {max} characters")
    private String content;
    @NotNull
    private UUID categoryId;
    @Builder.Default
    @Size(max = 10, message = "Only {max} tags are allowed")
    private Set<UUID> tagIds = new HashSet<>();
    @NotNull(message = "Status is required")
    private PostStatus status;
}
