package com.melaniia.blog.services.impl;

import com.melaniia.blog.domain.CreatePostRequest;
import com.melaniia.blog.domain.PostStatus;
import com.melaniia.blog.domain.UpdatePostRequest;
import com.melaniia.blog.domain.entities.Category;
import com.melaniia.blog.domain.entities.Post;
import com.melaniia.blog.domain.entities.Tag;
import com.melaniia.blog.domain.entities.User;
import com.melaniia.blog.repositories.PostRepository;
import com.melaniia.blog.services.CategoryService;
import com.melaniia.blog.services.PostService;
import com.melaniia.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId!=null && tagId!=null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository
                    .findAllByStatusAndCategoryAndTagsContaining(
                            PostStatus.PUBLISHED, category, tag);
        }
        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }
        if (tagId != null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDrafts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest postRequest) {
        Post post = new Post();
        post.setId(postRequest.getId());
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(user);
        Category category = categoryService.getCategoryById(postRequest.getCategoryId());
        post.setCategory(category);
        Set<UUID> tagIds = postRequest.getTagIds();
        Set<Tag> tags = new HashSet<>(tagService.getTagsByIds(tagIds));
        post.setTags(tags);
        post.setStatus((postRequest.getStatus()));
        post.setReadingTime(calculateReadingTime(post.getContent()));

        return postRepository.save(post);
    }

    private Integer calculateReadingTime(String content){
        if (content.isEmpty() || content == null){
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double)wordCount/WORDS_PER_MINUTE);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id" + id + " doesn't exist"));
        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());
        post.setStatus(updatePostRequest.getStatus());
        post.setReadingTime(calculateReadingTime(post.getContent()));

        UUID categoryId = updatePostRequest.getCategoryId();
        if(!post.getCategory().getId().equals(categoryId)){
            post.setCategory(categoryService.getCategoryById(categoryId));
        }
        Set<UUID> tagIds = updatePostRequest.getTagIds();
        Set<UUID> existingTagIds = post.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        if (!existingTagIds.equals(tagIds)){
            post.setTags(new HashSet<>(tagService.getTagsByIds(tagIds)));
        }
        System.out.println("Title: " + post.getTitle());
        System.out.println("Content: " + post.getContent());
        System.out.println("Status: " + post.getStatus());
        return postRepository.save(post);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post with id" + id + " doesn't exist")
        );
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }
}
