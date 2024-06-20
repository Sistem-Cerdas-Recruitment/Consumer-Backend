package com.BE.dto.job;

import com.BE.constants.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobStatusResponseDTO {
    private String message;
    private JobStatus status;
}
