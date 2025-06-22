// src/main/java/com/datasphere/backend/controllers/PostController.java
package com.datasphere.backend.controllers;

import com.datasphere.backend.dtos.PostDto;
import com.datasphere.backend.models.Post;
import com.datasphere.backend.services.PostService;
import com.datasphere.backend.utils.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final JwtTokenUtil jwtTokenUtil;

    public PostController(PostService postService,
                          JwtTokenUtil jwtTokenUtil) {
        this.postService  = postService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid PostDto postDto
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing or invalid Authorization header or Token"));
        }

        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtTokenUtil.extractUserEmail(token);

        Post created = postService.createPost(postDto, email);
        return ResponseEntity.ok(created);
    }
}
