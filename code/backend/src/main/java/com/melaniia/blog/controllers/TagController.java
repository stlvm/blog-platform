package com.melaniia.blog.controllers;

import com.melaniia.blog.domain.dtos.CreateTagsRequest;
import com.melaniia.blog.domain.dtos.TagDto;
import com.melaniia.blog.domain.entities.Tag;
import com.melaniia.blog.mappers.TagMapper;
import com.melaniia.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;
    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags(){
        List<TagDto> tags = tagService.getTags().stream()
                .map(tagMapper::toTagResponse)
                .toList();
        return ResponseEntity.ok(tags);
    }
    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@Valid @RequestBody CreateTagsRequest tagsRequest){
        List<Tag> tags = tagService.createTags(tagsRequest.getNames());
        List<TagDto> tagResponses = tags.stream()
                .map(tagMapper::toTagResponse)
                .toList();
        return  new ResponseEntity<>(tagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
    }
}
