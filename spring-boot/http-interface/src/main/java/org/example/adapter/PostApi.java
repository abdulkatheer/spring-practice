package org.example.adapter;

import org.example.dto.Post;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange(url = "http://localhost:8081/api/posts")
public interface PostApi {
    @GetExchange
    List<Post> getAll();

    @GetExchange("/{id}")
    Post getById(@PathVariable("id") int id);

    @PostExchange
    Post create(@RequestBody Post post);

    @PutExchange("/{id}")
    Post update(@PathVariable("id") int id, @RequestBody Post post);

    @DeleteExchange("/{id}")
    void delete(@PathVariable("id") int id);
}
