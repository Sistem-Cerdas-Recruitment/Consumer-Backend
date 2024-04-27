package com.BE.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class Job extends BaseEntity{

    private String title;
    private String description;

    @ManyToOne
    private User user;
}
