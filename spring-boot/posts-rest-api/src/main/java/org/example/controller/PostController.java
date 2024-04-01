package org.example.controller;

import org.example.dto.Post;
import org.example.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final Map<Integer, Post> postsDb = new HashMap<>();
    private static int counter = 0;

    @PostMapping
    public Post create(@RequestBody Post post) {
        post.setId(++counter);
        postsDb.put(post.getId(), post);
        return post;
    }

    @PutMapping("/{id}")
    public Post create(@PathVariable("id") Integer id, @RequestBody Post updatedPost) {
        Post postFromDb = postsDb.get(id);
        if (postFromDb == null) {
            throw new ResourceNotFoundException(String.valueOf(id));
        }
        updatedPost.setId(id);
        postsDb.put(id, updatedPost);
        return updatedPost;
    }

    @GetMapping
    public Collection<Post> getAll() {
        return postsDb.values();
    }

    @GetMapping("/{id}")
    public Post getOne(@PathVariable("id") Integer id) {
        Post postFromDb = postsDb.get(id);
        if (postFromDb == null) {
            throw new ResourceNotFoundException(String.valueOf(id));
        }
        return postFromDb;
    }

    @DeleteMapping("/{id}")
    public Post delete(@PathVariable("id") Integer id) {
        Post postFromDb = postsDb.get(id);
        if (postFromDb == null) {
            throw new ResourceNotFoundException(String.valueOf(id));
        }
        postsDb.remove(id);
        return postFromDb;
    }
}
