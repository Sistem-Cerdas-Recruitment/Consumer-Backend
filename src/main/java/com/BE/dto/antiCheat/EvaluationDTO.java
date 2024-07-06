package com.BE.dto.antiCheat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationDTO {
    @JsonProperty("predicted_class")
    private String predictedClass;

    @JsonProperty("confidence")
    private String confidence;

    @JsonProperty("secondary_model_probability")
    private String secondaryModelProbability;

    @JsonProperty("main_model_probability")
    private String mainModelProbability;
}
