package com.ms.music_service.dto;

import java.util.UUID;

public record CommentResponseDTO(
        Long commentId,
        Long musicId,
        UUID userId,
        String comment
) {
}