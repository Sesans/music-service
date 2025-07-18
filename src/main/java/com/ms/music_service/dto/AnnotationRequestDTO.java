package com.ms.music_service.dto;

public record AnnotationRequestDTO(
        String note,
        String excerpt,
        int startIndex,
        int endIndex
) {
}
