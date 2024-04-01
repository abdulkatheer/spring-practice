package org.example.adapter;

import org.example.dto.Post;
import org.example.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PostAdapter {
    private final String uriBase;

    RestClient restClient = RestClient.create();

    public PostAdapter(@Value("${post.api.base-url}") String uriBase) {
        this.uriBase = uriBase;
    }

    public List<Post> getAll() {
        List<Post> posts = restClient.get()
                .uri(uriBase + "/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return posts;
    }

    public Post create(Post post) {
        Post response = restClient.post()
                .uri(uriBase + "/posts")
                .body(post)
                .retrieve()
                .body(Post.class);
        return response;
    }

    public Post getById(int id) {
            Post post = restClient.get()
                    .uri(uriBase + "/posts/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new ResourceNotFoundException(String.valueOf(id));
                    })
                    .body(Post.class);
        return post;
    }

    public Post update(int id, Post post) {
        Post response = restClient.put()
                .uri(uriBase + "/posts/{id}", id)
                .body(post)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, responses) -> {
                    throw new ResourceNotFoundException(String.valueOf(id));
                })
                .body(Post.class);
        return response;
    }

    public void delete(int id){
        ResponseEntity<Void> response = restClient.delete()
                .uri(uriBase + "/posts/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, responses) -> {
                    throw new ResourceNotFoundException(String.valueOf(id));
                })
                .toBodilessEntity();
    }


//    public List<Post> getAll(){
//        Post po = new Post();
//        Post po1 = new Post();
//        po.setId(1);
//        po.setUserId(1);
//        po.setTitle("TestTitle");
//        po.setBody("TestBody");
//        po1.setId(2);
//        po1.setUserId(2);
//        po1.setTitle("SampleTitle");
//        po1.setBody("SampleBody");
//        List<Post> post = new ArrayList<>();
//        post.add(po);
//        post.add(po1);
//
//        return post;

}
