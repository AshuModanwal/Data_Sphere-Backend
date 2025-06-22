// src/main/java/com/datasphere/backend/repositories/PostRepository.java
package com.datasphere.backend.repositories;

import com.datasphere.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorId(Long authorId);

    // Order by the entity’s createdAt timestamp
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByAuthorIdInOrderByCreatedAtDesc(Set<Long> authorIds);
}
