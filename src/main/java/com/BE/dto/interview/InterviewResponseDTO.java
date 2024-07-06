package com.BE.dto.interview;

import com.BE.constants.InterviewStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewResponseDTO {
    private InterviewStatus status;
    private String response;
}
