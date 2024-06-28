package com.BE.dto.interview;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterviewScoreRequestDTO {
    @NotNull(message = "Job application ID cannot be null")
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    @NotNull(message = "Interview score cannot be null")
    @JsonProperty("interview_score")
    private Float interviewScore;
}
