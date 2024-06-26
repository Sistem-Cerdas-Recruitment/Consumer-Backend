package com.BE.entities;

import java.util.Date;
import java.util.List;

import com.BE.constants.JobStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Job extends BaseEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private JobStatus status;
    @Builder.Default
    private Integer applicants = 0;
    @Builder.Default
    private Integer offeredInterview = 0;
    @Builder.Default
    private Integer interviewed = 0;
    @Builder.Default
    private Integer yearsOfExperience = 0;
    private String location;
    private String salary;
    private List<String> advantages;
    private String additionalInfo;
    private String mode;
    private String type;
    private String experienceLevel;
    private List<String> responsibilities;
    private List<String> requirements;
    private List<String> majors;
    private List<String> skills;
    private Date closedAt;

    @ManyToOne
    private User user;
}
