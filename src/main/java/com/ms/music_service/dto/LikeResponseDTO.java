package com.ms.music_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LikeResponseDTO(
        Long likeId,
        UUID userId,
        Long musicId,
        LocalDateTime timestamp
) {
}
