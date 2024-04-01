package org.example.adapter;

import org.example.dto.Post;
import org.example.exception.ResourceNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class PostAdapter {
    private static final String uriBase = "http://localhost:8081/api";
    RestTemplate restTemplate = new RestTemplate();

    public List<Post> getAll() {
        Post[] posts = restTemplate.getForObject(uriBase + "/posts", Post[].class);
        return Arrays.asList(posts);
    }

    public Post create(Post post) {
        HttpEntity<Post> request = new HttpEntity<>(post);
        Post response = restTemplate.postForObject(uriBase + "/posts", request, Post.class);
        return response;
    }

    public Post getById(int id) {
        try {
            Post post = restTemplate.getForObject(uriBase + "/posts/" + id, Post.class);
            return post;
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ResourceNotFoundException(String.valueOf(id));
        }
    }

    public Post update(int id, Post post) {
        try {
            HttpEntity<Post> request = new HttpEntity<>(post);
            Post response = restTemplate.exchange(uriBase + "/posts/" + id, HttpMethod.PUT, request, Post.class).getBody();
            return response;
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ResourceNotFoundException(String.valueOf(id));
        }
    }

    public void delete(int id){
        try {
            restTemplate.delete(uriBase + "/posts/" + id, Post.class);
        }
        catch(HttpClientErrorException.NotFound e){
            throw new ResourceNotFoundException(String.valueOf(id));
        }
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
