package com.BE.dto.job;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
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

    private String salary;
    private List<String> advantages;
    @JsonProperty("additional_info")
    private String additionalInfo;
    private String mode;
    private String type;
    private String location;
    @JsonProperty("experience_level")
    private String experienceLevel;
    private List<String> responsibilities;
    private List<String> requirements;
}
