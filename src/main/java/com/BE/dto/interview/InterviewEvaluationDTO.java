package com.BE.dto.interview;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewEvaluationDTO {
    private UUID jobApplicationId;
    private List<String> competences;
    private List<List<InterviewChatDTO>> transcript;
}
