package com.BE.entities;

import java.util.List;

import org.hibernate.annotations.Type;

import com.BE.constants.Evaluation;
import com.BE.constants.JobApplicationStatus;
import com.BE.dto.InterviewChatLogDTO;

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

    private Evaluation evaluation;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<InterviewChatLogDTO> interviewChatLogs;
    
    @ManyToOne
    private User user;

    @ManyToOne
    private Job job;

    @ManyToOne
    private CurriculumVitae cv;
}
