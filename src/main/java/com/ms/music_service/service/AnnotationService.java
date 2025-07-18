package com.ms.music_service.service;

import com.ms.music_service.dto.AnnotationRequestDTO;
import com.ms.music_service.dto.AnnotationResponseDTO;

import java.util.List;

public interface AnnotationService {
    List<AnnotationResponseDTO> findAll(Long musicId);

    AnnotationResponseDTO submitAnnotation(Long musicId, AnnotationRequestDTO requestDTO);
}
