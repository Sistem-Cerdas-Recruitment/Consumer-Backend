package com.BE.dto.interview;

import com.BE.constants.InterviewStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterviewResponseDTO {
    private InterviewStatus status;
    private String response;
}
