package com.BE.entities;

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
public class JobApplication extends BaseEntity{
    
    @ManyToOne
    private User user;

    @ManyToOne
    private Job job;

    @ManyToOne
    private CurriculumVitae cv;
}