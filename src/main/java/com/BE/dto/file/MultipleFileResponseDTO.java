package com.BE.dto.file;

import java.util.List;

import org.springframework.data.domain.Page;

import com.BE.entities.CurriculumVitae;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleFileResponseDTO {
    List<FileResponseDTO> files;

    public MultipleFileResponseDTO(Page<CurriculumVitae> cvPage) {
        this.files = cvPage.map(cv -> FileResponseDTO.builder().id(cv.getId()).fileName(cv.getOriginalFileName())
                .uploadDate(cv.getCreatedAt()).build()).getContent();
    }
}
