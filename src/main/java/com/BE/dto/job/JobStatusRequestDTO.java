package com.BE.dto.job;

import java.util.UUID;

import lombok.Data;

@Data
public class JobStatusRequestDTO {
    private UUID jobId;
    private Boolean status;
}
