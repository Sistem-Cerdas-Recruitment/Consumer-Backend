package com.BE.dto.interview;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterviewStartRequestDTO {
    @JsonProperty("job_application_id")
    @NotBlank(message = "Job application id is required")
    private UUID jobApplicationId;
}
