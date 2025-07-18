package com.ms.music_service.dto.music;

public record MusicRequestDTO(
        String title,
        Long artistId,
        String album,
        String genre,
        String lyrics
) {
}
