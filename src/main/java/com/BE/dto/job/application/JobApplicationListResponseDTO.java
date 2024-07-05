package com.BE.dto.job.application;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationListResponseDTO {
    List<JobApplicationResultDTO> data;
}
