package com.ms.music_service.dto;

public record MusicPageDTO(
        Long id,
        String title,
        String artist,
        int likeCount,
        int commentCount
) {
}
