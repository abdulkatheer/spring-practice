package org.example.adapter;

import org.example.dto.Post;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Component
public class PostAdapter {
    RestTemplate restTemplate = new RestTemplate();
    RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    PostApi service = factory.createClient(PostApi.class);

    public List<Post> getAll() {
        return service.getAll();
    }

    public Post create(Post post) {
        return service.create(post);
    }

    public Post getById(int id) {
        try {
            return service.getById(id);
        }
        catch(HttpClientErrorException.NotFound e){
            throw new ResourceNotFoundException(String.valueOf(id));
        }
    }

    public Post update(int id, Post post) {
        try {
            return service.update(id, post);
        }
        catch (HttpClientErrorException.NotFound e){
            throw new ResourceNotFoundException(String.valueOf(id));
        }
    }

    public void delete(int id) {
        try {
            service.delete(id);
        }
        catch (HttpClientErrorException.NotFound e){
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
