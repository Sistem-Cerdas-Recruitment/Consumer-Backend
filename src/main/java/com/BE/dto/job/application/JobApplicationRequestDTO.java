package com.BE.dto.job.application;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationRequestDTO {
    private UUID jobId;
    private UUID cvId;
    private Object experience;
}
