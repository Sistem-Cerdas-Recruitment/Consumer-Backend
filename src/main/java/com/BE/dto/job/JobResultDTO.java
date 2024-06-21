package com.BE.dto.job;

import java.time.LocalDateTime;
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
    private List<String> majors;
    private List<String> skills;
    private JobStatus status;
    private Integer applicants;
    private Integer offeredInterview;
    private Integer interviewed;
    private UUID userId;
    private String company;
    private String location;
    private Boolean applied;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
}
