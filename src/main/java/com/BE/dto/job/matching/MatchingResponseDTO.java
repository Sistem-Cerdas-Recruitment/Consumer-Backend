package com.BE.dto.job.matching;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MatchingResponseDTO {
    @JsonProperty("score")
    private Double relevanceScore;

    @JsonProperty("is_accepted")
    private Boolean isRelevant;
}
