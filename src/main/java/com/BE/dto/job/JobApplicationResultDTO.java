package com.BE.dto.job;

import java.util.UUID;

import com.BE.constants.JobApplicationStatus;

import lombok.Data;

@Data
public class JobApplicationResultDTO {
    private UUID id;
    private JobApplicationStatus status;
    private UUID jobId;
    private String jobTitle;
    private UUID userId;
    private String userName;
}
