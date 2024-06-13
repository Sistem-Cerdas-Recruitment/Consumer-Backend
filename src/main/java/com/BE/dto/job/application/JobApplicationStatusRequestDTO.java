package com.BE.dto.job.application;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JobApplicationStatusRequestDTO {
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    @JsonProperty("is_accepted")
    private Boolean isAccepted;
}
