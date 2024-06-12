package com.BE.dto.interview;

import java.util.List;

import com.BE.constants.InterviewStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InterviewDTO {
    private InterviewStatus status;
    @JsonProperty("chat_logs")
    private List<InterviewChatDTO> chatLogs;

    public InterviewDTO(InterviewStatus status, InterviewChatHistoryDTO chatHistory) {
        this.status = status;
        this.chatLogs = chatHistory.getChatHistories().stream().flatMap(List::stream).toList();
    }
}
