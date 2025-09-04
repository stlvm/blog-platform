package com.melaniia.blog.services.impl;

import com.melaniia.blog.domain.entities.User;
import com.melaniia.blog.repositories.UserRepository;
import com.melaniia.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                 new EntityNotFoundException("User with id" + id + " not found"));
    }
}
