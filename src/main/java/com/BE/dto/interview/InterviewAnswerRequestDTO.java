package com.BE.dto.interview;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterviewAnswerRequestDTO {
    @JsonProperty("job_application_id")
    @NotNull(message = "Job application ID cannot be null")
    private UUID jobApplicationId;
    @NotNull(message = "Is accepted cannot be null")
    private InterviewChatDTO chat;
}
