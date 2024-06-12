package com.BE.dto.interview;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewChatHistoryDTO {
    @Builder.Default
    private int competencyIndex = 0;
    private List<String> competencies;
    private List<List<InterviewChatDTO>> chatHistories;
}
