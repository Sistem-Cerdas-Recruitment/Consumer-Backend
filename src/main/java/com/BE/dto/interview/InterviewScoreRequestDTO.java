package com.BE.dto.interview;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InterviewScoreRequestDTO {
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    @JsonProperty("interview_score")
    private Float interviewScore;
}
