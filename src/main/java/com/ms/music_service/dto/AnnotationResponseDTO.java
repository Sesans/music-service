package com.ms.music_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnnotationResponseDTO(
        Long annotationId,
        UUID userId,
        Long musicId,
        String note,
        String excerpt,
        int startIndex,
        int endIndex,
        LocalDateTime timestamp
) {
}
