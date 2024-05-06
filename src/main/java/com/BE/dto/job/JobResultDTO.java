package com.BE.dto.job;

import java.util.UUID;

import com.BE.constants.JobStatus;

import lombok.Data;

@Data
public class JobResultDTO {
    private UUID id;
    private String title;
    private String description;
    private JobStatus status;
    private UUID userId;
    private String name;
    private Boolean applied;
}
