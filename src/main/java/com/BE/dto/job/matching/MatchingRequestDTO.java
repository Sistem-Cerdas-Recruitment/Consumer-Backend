package com.BE.dto.job.matching;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingRequestDTO {
    private Object cv;
    private JobMatchingDTO job;
}
