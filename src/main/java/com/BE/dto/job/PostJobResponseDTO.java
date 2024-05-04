package com.BE.dto.job;

import com.BE.entities.Job;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostJobResponseDTO {
    private String message;
    private Job job;
}
