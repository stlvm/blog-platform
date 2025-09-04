package com.melaniia.blog.services;

import com.melaniia.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
