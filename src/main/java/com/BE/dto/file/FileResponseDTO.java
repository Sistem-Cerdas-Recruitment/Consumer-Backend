package com.BE.dto.file;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {
    private UUID id;
    private String fileName;
    private LocalDateTime uploadDate;
}
