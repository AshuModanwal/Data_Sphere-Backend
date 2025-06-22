// src/main/java/com/datasphere/backend/services/impl/PostImpl.java
package com.datasphere.backend.services.impl;

import com.datasphere.backend.dtos.PostDto;
import com.datasphere.backend.models.Post;
import com.datasphere.backend.models.User;
import com.datasphere.backend.repositories.PostRepository;
import com.datasphere.backend.repositories.UserRepository;
import com.datasphere.backend.services.PostService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PostImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostImpl(UserRepository userRepository,
                           PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(PostDto postDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setAuthor(user);
        post.setContent(postDto.getContent());
        return postRepository.save(post);
    }
}
