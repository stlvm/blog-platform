package com.melaniia.blog.services;

import com.melaniia.blog.domain.CreatePostRequest;
import com.melaniia.blog.domain.UpdatePostRequest;
import com.melaniia.blog.domain.entities.Post;
import com.melaniia.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDrafts(User user);
    Post createPost(User user, CreatePostRequest postRequest);
    Post updatePost(UUID uuid, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);
}
