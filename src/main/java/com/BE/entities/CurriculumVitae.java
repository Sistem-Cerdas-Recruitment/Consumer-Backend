package com.BE.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class CurriculumVitae extends BaseEntity {

    private String fileName;
    private String originalFileName;
    private String description;
    
    @Builder.Default
    @Column(columnDefinition = "boolean default false")
    private boolean isDefault = false;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
