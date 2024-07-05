package com.BE.dto.job;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobStatusRequestDTO {
    @NotNull(message = "Job ID cannot be null")
    private UUID jobId;
    @NotNull(message = "Status cannot be null")
    private Boolean status;
}
