package com.BE.dto.file;

import java.util.List;

import com.BE.entities.CurriculumVitae;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultipleFileResponseDTO {
    List<FileResponseDTO> files;

    public MultipleFileResponseDTO(List<CurriculumVitae> cvPage) {
        this.files = cvPage.stream()
                .map(cv -> FileResponseDTO.builder().id(cv.getId()).fileName(cv.getOriginalFileName())
                        .isDefault(cv.isDefault())
                        .uploadDate(cv.getCreatedAt()).build())
                .toList();
    }
}
