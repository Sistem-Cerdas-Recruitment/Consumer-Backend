package com.BE.dto.interview;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterviewAnswerRequestDTO {
    @JsonProperty("job_application_id")
    @NotBlank(message = "Job application id is required")
    private UUID jobApplicationId;
    @NotBlank(message = "Answer is required")
    private InterviewChatDTO chat;
}
