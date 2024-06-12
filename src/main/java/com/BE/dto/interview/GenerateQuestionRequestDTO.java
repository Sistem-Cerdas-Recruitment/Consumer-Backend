package com.BE.dto.interview;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateQuestionRequestDTO {
    private String competence;
    private List<InterviewChatDTO> transcript;
}
