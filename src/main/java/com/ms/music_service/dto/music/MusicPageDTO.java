package com.ms.music_service.dto.music;

public record MusicPageDTO(
        Long id,
        String title,
        String artist,
        boolean liked,
        int likeCount,
        int commentCount
) {
}
