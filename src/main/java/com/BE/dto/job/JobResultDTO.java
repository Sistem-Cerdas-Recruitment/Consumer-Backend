package com.BE.dto.job;

import java.util.List;
import java.util.UUID;

import com.BE.constants.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobResultDTO {
    private UUID id;
    private String title;
    private String description;
    private List<String> priorityRoles;
    private List<String> skills;
    private JobStatus status;
    private UUID userId;
    private String name;
    private Boolean applied;
}
