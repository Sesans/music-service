package com.ms.music_service.dto;

public record MusicResponseDTO(
        Long id,
        String title,
        String compositor,
        String album,
        String genre,
        String lyrics,
        int likeCount,
        int commentCount
) {
}
