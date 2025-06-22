// src/main/java/com/datasphere/backend/services/PostService.java
package com.datasphere.backend.services;

import com.datasphere.backend.dtos.PostDto;
import com.datasphere.backend.models.Post;

public interface PostService {
    Post createPost(PostDto postDto, String email);
}
