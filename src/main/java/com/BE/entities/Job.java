package com.BE.entities;

import java.util.List;

import com.BE.constants.JobStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String description;
    private JobStatus status;
    @JsonProperty("priority_majors")
    private List<String> priorityMajors;
    private List<String> skills;

    @ManyToOne
    private User user;
}
