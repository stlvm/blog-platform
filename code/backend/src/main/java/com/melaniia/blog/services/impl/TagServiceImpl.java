package com.melaniia.blog.services.impl;

import com.melaniia.blog.domain.entities.Tag;
import com.melaniia.blog.repositories.TagRepository;
import com.melaniia.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }
    @Transactional
    @Override
    public List<Tag> createTags(Set<String> names) {
         List<Tag> existingTags = tagRepository.findByNameIn(names);
         Set<String> existingTagNames = existingTags.stream()
                 .map(Tag::getName)
                 .collect(Collectors.toSet());
        List<Tag> newTags = names.stream()
                .filter(name-> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();
        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()){
            savedTags = tagRepository.saveAll(newTags);
        }
        savedTags.addAll(existingTags);
        return savedTags;
    }

    @Transactional
    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag ->
        {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete the tag with posts");
            }
            tagRepository.deleteById(id);
        });
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Tag doesn't exist")
        );
    }

    @Override
    public List<Tag> getTagsByIds(Set<UUID> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if(tags.size() != tagIds.size()){
            throw new EntityNotFoundException();
        }
        return tags;
    }
}
