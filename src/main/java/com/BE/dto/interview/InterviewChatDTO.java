package com.BE.dto.interview;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewChatDTO {
    private String question;
    private String answer;

    @JsonProperty("backspace_count")
    private int backspaceCount;

    @JsonProperty("letter_click_counts")
    private Map<String, Integer> letterClickCounts;

    /*
     *  Evaluation
     */

    @JsonProperty("predicted_class")
    private String predictedClass;

    @JsonProperty("confidence")
    private String confidence;

    @JsonProperty("secondary_model_prediction")
    private String secondaryModelPrediction;

    @JsonProperty("main_model_probability")
    private String mainModelProbability;
}
