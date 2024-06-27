package com.BE.dto.job;

import java.util.Date;
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
    private Integer minYearsOfExperience;
    private Integer applicants;
    private Integer offeredInterview;
    private Integer interviewed;
    private Boolean applied;
    private UUID userId;
    private String company;
    private String location;
    private String salary;
    private List<String> advantages;
    private String additionalInfo;
    private String mode;
    private String type;
    private String experienceLevel;
    private List<String> responsibilities;
    private List<String> requirements;
    private Date createdAt;
    private Date updatedAt;
    private Date closedAt;
}
