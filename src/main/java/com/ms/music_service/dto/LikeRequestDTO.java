package com.ms.music_service.dto;

import java.util.UUID;

public record LikeRequestDTO(
        UUID userId,
        Long musicId
) {
}
