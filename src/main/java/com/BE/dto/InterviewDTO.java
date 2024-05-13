package com.BE.dto;

import java.util.List;

import com.BE.constants.InterviewStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterviewDTO {
    private InterviewStatus status;
    @JsonProperty("chat_logs")
    private List<InterviewChatLogDTO> chatLogs;
}
