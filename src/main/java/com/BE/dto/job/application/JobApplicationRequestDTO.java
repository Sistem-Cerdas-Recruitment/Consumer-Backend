package com.BE.dto.job.application;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationRequestDTO {
    @NotNull(message = "Job ID cannot be null")
    @JsonProperty("job_id")
    private UUID jobId;
    @NotNull(message = "CV ID cannot be null")
    @JsonProperty("cv_id")
    private UUID cvId;
    private Object experience;
}
