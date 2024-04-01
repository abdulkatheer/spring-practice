package org.example.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.dto.Post;
import org.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.Options.DYNAMIC_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {"post.api.base-url=http://localhost:${wiremock.server.port}/api"})
@AutoConfigureWireMock(port = DYNAMIC_PORT)
class PostAdapterIT {
    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private PostAdapter postAdapter;

    @Test
    void getAllShouldReturnEmptyListWhenNoPostExists() throws JsonProcessingException {
        List<Post> post = Collections.emptyList();
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(post);
        wireMockServer.stubFor(WireMock.get("/api/posts")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(response)));

       List<Post> result = postAdapter.getAll();

       assertEquals(0, result.size());
    }
    @Test
    void getAllShouldReturnListWhenPostsExists() throws JsonProcessingException {
        Post post = new Post();
        post.setId(1);
        post.setUserId(2);
        post.setTitle("TestTitle");
        post.setBody("TestBody");
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        ObjectMapper object = new ObjectMapper();
        String po = object.writeValueAsString(posts);
        wireMockServer.stubFor(WireMock.get("/api/posts")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(po)));

        List<Post> result = postAdapter.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void createShouldReturnResponseWhenPostHappens() throws JsonProcessingException {
        Post post = new Post();
        post.setId(1);
        post.setUserId(2);
        post.setTitle("TestTitle");
        post.setBody("TestBody");
        ObjectMapper object = new ObjectMapper();
        String po = object.writeValueAsString(post);
        wireMockServer.stubFor(WireMock.post("/api/posts")
                        .withRequestBody(equalToJson(po))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(po)));

        Post result = postAdapter.create(post);

        assertEquals(1, result.getId());
        assertEquals("TestBody", result.getBody());
        assertEquals("TestTitle", result.getTitle());
        assertEquals(2, result.getUserId());
    }

    @Test
    void getByIdShouldReturnSingleResponseWhenGetHappens() throws JsonProcessingException {
        Post post = new Post();
        post.setId(1);
        post.setUserId(2);
        post.setTitle("TestTitle");
        post.setBody("TestBody");
        ObjectMapper object = new ObjectMapper();
        String po = object.writeValueAsString(post);
        wireMockServer.stubFor(WireMock.get("/api/posts/1")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(po)));

        Post result = postAdapter.getById(1);

        assertEquals(1, result.getId());
        assertEquals("TestBody", result.getBody());
        assertEquals("TestTitle", result.getTitle());
        assertEquals(2, result.getUserId());
    }

    @Test
    void getByIdShouldReturnBotFoundWhenNoGivenIdIsThere() throws JsonProcessingException {

        wireMockServer.stubFor(WireMock.get("/api/posts/1")
                .willReturn(WireMock.aResponse()
                        .withStatus(404)
                        .withHeader("Content-type", "application/json")));

        ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> postAdapter.getById(1));
        assertEquals("1", result.getId());
    }
}
