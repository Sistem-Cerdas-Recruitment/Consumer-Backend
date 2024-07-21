package com.BE.dto.job.matching;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingRequestDTO {
    private UUID jobApplicationId;
    private Object cv;
    private JobMatchingDTO job;
}
