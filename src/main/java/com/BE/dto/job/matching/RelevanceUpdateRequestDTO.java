package com.BE.dto.job.matching;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelevanceUpdateRequestDTO {
    private UUID jobApplicationId;
    @JsonProperty("score")
    private Double relevanceScore;

    @JsonProperty("is_accepted")
    private Boolean isRelevant;
}
