package com.BE.dto.job.application;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationStatusRequestDTO {
    @NotNull(message = "Job application ID cannot be null")
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    @NotNull(message = "Is accepted cannot be null")
    @JsonProperty("is_accepted")
    private Boolean isAccepted;
}
