package com.BE.dto.antiCheat;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AntiCheatResultDTO {
    @JsonProperty("job_application_id")
    private UUID jobApplicationId;
    private List<EvaluationDTO> evaluations;
}
