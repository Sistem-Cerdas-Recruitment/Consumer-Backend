package com.BE.entities;

import com.BE.constants.JobStatus;

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
public class Job extends BaseEntity{

    private String title;
    private String description;
    private JobStatus status;

    @ManyToOne
    private User user;
}
