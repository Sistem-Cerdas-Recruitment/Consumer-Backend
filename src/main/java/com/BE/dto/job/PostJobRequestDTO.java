package com.BE.dto.job;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostJobRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Priority roles are required")
    private List<String> priorityMajors;
    @NotBlank(message = "Skills are required")
    private List<String> skills;
}
