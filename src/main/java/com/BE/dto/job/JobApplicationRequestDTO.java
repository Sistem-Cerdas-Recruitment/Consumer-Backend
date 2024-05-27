package com.BE.dto.job;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationRequestDTO {
    private UUID jobId;
    private UUID cvId;
}
