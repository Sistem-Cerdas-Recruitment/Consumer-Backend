package com.BE.entities;

import org.hibernate.annotations.Type;

import com.BE.constants.JobApplicationStatus;
import com.BE.dto.interview.InterviewChatHistoryDTO;

import io.hypersistence.utils.hibernate.type.json.JsonType;
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
public class JobApplication extends BaseEntity{

    private JobApplicationStatus status;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private InterviewChatHistoryDTO interviewChatHistory;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Object experience;

    private Double relevanceScore;

    private Float interviewScore;

    private Boolean isRelevant;
    
    @ManyToOne
    private User user;

    @ManyToOne
    private Job job;

    @ManyToOne
    private CurriculumVitae cv;
}
