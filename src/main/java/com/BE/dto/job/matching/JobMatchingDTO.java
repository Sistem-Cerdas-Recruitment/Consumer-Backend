package com.BE.dto.job.matching;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobMatchingDTO {
    private int minYoE;
    private String role;
    private String jobDesc;
    private List<String> majors;
    private List<String> skills;
}
