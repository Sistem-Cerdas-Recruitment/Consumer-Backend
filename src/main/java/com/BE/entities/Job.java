package com.BE.entities;

import java.time.LocalDateTime;
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
    private Integer yearsOfExperience;
    private String location;
    private List<String> majors;
    private List<String> skills;
    private LocalDateTime closedAt;

    @ManyToOne
    private User user;
}
