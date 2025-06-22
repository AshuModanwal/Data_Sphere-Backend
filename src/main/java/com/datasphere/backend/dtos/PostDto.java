// src/main/java/com/datasphere/backend/dtos/PostDto.java
package com.datasphere.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostDto {

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 500, message = "Content must be between 10 and 500 characters")
    private String content;

    public PostDto() {}

    public PostDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
