package com.BE.dto.job.matching;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingRequestDTO {
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    private MatchingRequestDataDTO data;
}
