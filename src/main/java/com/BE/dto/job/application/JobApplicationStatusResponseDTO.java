package com.BE.dto.job.application;

import com.BE.constants.JobApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationStatusResponseDTO {
    private JobApplicationStatus status;
}
