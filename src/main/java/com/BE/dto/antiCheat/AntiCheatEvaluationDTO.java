package com.BE.dto.antiCheat;

import java.util.List;
import java.util.UUID;

import com.BE.dto.InterviewChatDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AntiCheatEvaluationDTO {
    @NotBlank(message = "Job application id is required")
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;

    @NotBlank(message = "Evaluation is required")
    @JsonProperty("data")
    private List<InterviewChatDTO> data;
}
