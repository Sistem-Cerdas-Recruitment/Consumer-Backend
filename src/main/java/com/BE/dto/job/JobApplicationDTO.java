package com.BE.dto.job;

import java.util.UUID;

import com.BE.constants.JobApplicationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobApplicationDTO {
    private UUID id;
    private JobApplicationStatus status;
    private UUID jobId;
    private String jobTitle;
    private UUID recruiterId;
    private String recruiterName;
    private UUID userId;
    private String userName;
    private String fileName;
    private String cvUrl;
}
