package com.BE.dto.job;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostJobResponseDTO {
    private String message;
    private JobResultDTO job;
}
