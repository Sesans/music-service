package com.ms.music_service.dto;

import java.util.UUID;

public record CommentDTO(
        Long musicId,
        UUID userId,
        String comment
) {
}