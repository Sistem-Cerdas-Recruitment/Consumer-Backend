package com.BE.dto.job;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostJobRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @JsonProperty("years_of_experience")
    @NotBlank(message = "Years of experience is required")
    private int yearsOfExperience;

    @NotBlank(message = "majors are required")
    @NotEmpty(message = "majors cannot be empty")
    private List<String> majors;

    @NotBlank(message = "Skills are required")
    @NotEmpty(message = "Skills cannot be empty")
    private List<String> skills;
}
