package com.BE.dto.job.matching;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelevanceUpdateRequestDTO {
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    @JsonProperty("score")
    private Double relevanceScore;

    @JsonProperty("is_accepted")
    private Boolean isRelevant;
}
