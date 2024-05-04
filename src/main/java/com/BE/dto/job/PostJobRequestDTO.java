package com.BE.dto.job;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostJobRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;

}
