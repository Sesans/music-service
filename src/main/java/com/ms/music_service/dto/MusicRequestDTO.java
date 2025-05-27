package com.ms.music_service.dto;

public record MusicRequestDTO(
        String title,
        String artist,
        String album,
        String genre,
        String lyrics
) {
}
