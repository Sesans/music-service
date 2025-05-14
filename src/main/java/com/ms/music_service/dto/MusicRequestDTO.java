package com.ms.music_service.dto;

public record MusicRequestDTO(
        String title,
        String compositor,
        String album,
        String genre,
        String lyrics,
        int likeCount
) {
}
