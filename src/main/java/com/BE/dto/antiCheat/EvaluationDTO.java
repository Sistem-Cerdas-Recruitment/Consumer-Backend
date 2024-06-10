package com.BE.dto.antiCheat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EvaluationDTO {
    @JsonProperty("predicted_class")
    private String predictedClass;

    @JsonProperty("confidence")
    private String confidence;

    @JsonProperty("secondary_model_prediction")
    private String secondaryModelPrediction;

    @JsonProperty("main_model_probability")
    private String mainModelProbability;
}
