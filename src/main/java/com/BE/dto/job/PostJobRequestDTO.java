package com.BE.dto.job;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostJobRequestDTO {

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Description cannot be null")
    private String description;

    @JsonProperty("years_of_experience")
    @NotNull(message = "Years of experience cannot be null")
    private Integer yearsOfExperience;

    @NotNull(message = "Majors cannot be null")
    private List<String> majors;

    @NotNull(message = "Skills cannot be null")
    private List<String> skills;

    @NotNull(message = "Salary cannot be null")
    private String salary;
    
    @NotNull(message = "Advantages cannot be null")
    private List<String> advantages;

    @NotNull(message = "Additional info cannot be null")
    @JsonProperty("additional_info")
    private String additionalInfo;

    @NotNull(message = "Mode cannot be null")
    private String mode;

    @NotNull(message = "Type cannot be null")
    private String type;

    @NotNull(message = "Location cannot be null")
    private String location;

    @NotNull(message = "Experience level cannot be null")
    @JsonProperty("experience_level")
    private String experienceLevel;

    @NotNull(message = "Responsibilities cannot be null")
    private List<String> responsibilities;

    @NotNull(message = "Requirements cannot be null")
    private List<String> requirements;
}
