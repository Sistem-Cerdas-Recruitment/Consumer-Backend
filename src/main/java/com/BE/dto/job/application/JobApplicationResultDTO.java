package com.BE.dto.job.application;

import java.util.UUID;

import com.BE.constants.JobApplicationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobApplicationResultDTO {
    private UUID id;
    private JobApplicationStatus status;
    private UUID jobId;
    private String jobTitle;
    private UUID recruiterId;
    private String recruiterName;
    private UUID userId;
    private Double relevanceScore;
    private Boolean isRelevant;
    private Float interviewScore;
    private String userName;
}
