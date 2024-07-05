package com.BE.dto.job.application;

import com.BE.constants.JobApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationStatusResponseDTO {
    private JobApplicationStatus status;
}
