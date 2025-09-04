package com.melaniia.blog.services;

import com.melaniia.blog.domain.entities.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> names);
    void deleteTag(UUID id);
    Tag getTagById(UUID id);
    List<Tag> getTagsByIds(Set<UUID> tagIds);
}
