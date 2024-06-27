package com.BE.dto.job.application;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobApplicationRequestDTO {
    @JsonProperty("job_id")
    private UUID jobId;
    @JsonProperty("cv_id")
    private UUID cvId;
    private Object experience;
    private String salary;
    private List<String> advantages;
    @JsonProperty("additional_info")
    private String additionalInfo;
    private String mode;
    private String type;
    private String experienceLevel;
    private List<String> responsibilities;
    private List<String> requirements;
}
