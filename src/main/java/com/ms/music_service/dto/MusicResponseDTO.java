package com.ms.music_service.dto;

public record MusicResponseDTO(
        Long id,
        String title,
        String compositor,
        String album,
        String genre,
        String lyrics,
        boolean liked,
        int likeCount,
        int commentCount
) {
}
