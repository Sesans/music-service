package com.ms.music_service.dto;

public record MusicResponseDTO(
        Integer id,
        String title,
        String compositor,
        String album,
        String genre,
        String lyrics,
        int likeCount
) {
}
