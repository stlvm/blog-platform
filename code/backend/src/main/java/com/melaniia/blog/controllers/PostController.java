package com.melaniia.blog.controllers;

import com.melaniia.blog.domain.CreatePostRequest;
import com.melaniia.blog.domain.PostStatus;
import com.melaniia.blog.domain.UpdatePostRequest;
import com.melaniia.blog.domain.dtos.CreatePostRequestDto;
import com.melaniia.blog.domain.dtos.PostDto;
import com.melaniia.blog.domain.dtos.UpdatePostRequestDto;
import com.melaniia.blog.domain.entities.Post;
import com.melaniia.blog.domain.entities.User;
import com.melaniia.blog.mappers.PostMapper;
import com.melaniia.blog.services.PostService;
import com.melaniia.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId
            ){
        List<PostDto> postDtos = postService.getAllPosts(categoryId, tagId)
                .stream()
                .map(postMapper::toDto)
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId){
        User loggedInUser = userService.getUserById(userId);
        List<Post> drafts = postService.getDrafts(loggedInUser);
        List<PostDto> postDtos = drafts.stream().map(postMapper::toDto).toList();
        return  ResponseEntity.ok(postDtos);
    }
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId){
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post post = postService.createPost(loggedInUser, createPostRequest);
        return new ResponseEntity(postMapper.toDto(post), HttpStatus.CREATED);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid  @RequestBody UpdatePostRequestDto updatePostRequestDto
            ){
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post post = postService.updatePost(id, updatePostRequest);
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id){
        Post post = postService.getPost(id);
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
