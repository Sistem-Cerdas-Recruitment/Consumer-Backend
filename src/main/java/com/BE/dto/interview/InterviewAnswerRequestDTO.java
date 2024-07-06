package com.BE.dto.interview;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewAnswerRequestDTO {
    @JsonProperty("job_application_id")
    @NotNull(message = "Job application ID cannot be null")
    private UUID jobApplicationId;
    @NotNull(message = "Chat cannot be null")
    private InterviewChatDTO chat;
}
