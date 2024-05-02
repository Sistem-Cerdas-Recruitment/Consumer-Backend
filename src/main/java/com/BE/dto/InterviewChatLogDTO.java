package com.BE.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewChatLogDTO {
    private String question;
    private String answer;

    @JsonProperty("backspace_count")
    private int backspaceCount;

    @JsonProperty("typing_duration")
    private int typingDuration;

    @JsonProperty("letter_click_counts")
    private Map<String, Integer> letterClickCounts;
}
