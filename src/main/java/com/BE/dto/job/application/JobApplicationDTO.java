package com.BE.dto.job.application;

import java.util.UUID;

import com.BE.constants.JobApplicationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobApplicationDTO {
    private UUID id;
    private JobApplicationStatus status;
    private Object experience;
    private Double relevanceScore;
    private Boolean isRelevant;
    private UUID jobId;
    private String jobTitle;
    private UUID recruiterId;
    private String recruiterName;
    private UUID userId;
    private String userName;
    private String fileName;
    private String cvUrl;
}
