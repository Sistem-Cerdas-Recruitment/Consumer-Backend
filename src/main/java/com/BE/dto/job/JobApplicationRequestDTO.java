package com.BE.dto.job;

import java.util.UUID;

import lombok.Data;

@Data
public class JobApplicationRequestDTO {
    private UUID jobId;
    private UUID cvId;
}
