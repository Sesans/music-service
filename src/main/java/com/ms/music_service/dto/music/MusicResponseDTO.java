package com.ms.music_service.dto.music;

public record MusicResponseDTO(
        Long id,
        String title,
        String artist,
        String album,
        String genre,
        String lyrics,
        boolean liked,
        int likeCount,
        int commentCount
) {
}
