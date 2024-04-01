package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.adapter.PostAdapter;
import org.example.dto.Post;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostAdapter postAdapter;

    @GetMapping("api/posts")
    public List<Post> getPosts() {
        return postAdapter.getAll();
    }
    @PostMapping("api/posts")
    public Post createPosts(@RequestBody Post post){
        return postAdapter.create(post);
    }
    @GetMapping("api/posts/{id}")
    public Post getPostById(@PathVariable("id") int id){
        return postAdapter.getById(id);
    }

    @PutMapping("api/posts/{id}")
    public Post updatePosts(@PathVariable("id") int id, @RequestBody Post post) {
        return postAdapter.update(id, post);
    }
    @DeleteMapping("api/posts/{id}")
    public void deletePosts(@PathVariable("id") int id){
         postAdapter.delete(id);
    }
}
