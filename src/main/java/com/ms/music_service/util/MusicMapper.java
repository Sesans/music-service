package com.ms.music_service.util;

import com.ms.music_service.domain.Artist;
import com.ms.music_service.domain.Music;
import com.ms.music_service.dto.music.MusicPageDTO;
import com.ms.music_service.dto.music.MusicRequestDTO;
import com.ms.music_service.dto.music.MusicResponseDTO;

public class MusicMapper {
    public static Music requestToEntity(MusicRequestDTO dto, Artist artist){
        return new Music(
                null,
                dto.title(),
                artist,
                dto.album(),
                dto.genre(),
                dto.lyrics(),
                false,
                0,
                0
        );
    }

    public static MusicResponseDTO entityToResponseDTO(Music music){
        return new MusicResponseDTO(
                music.getId(),
                music.getTitle(),
                music.getArtist().getName(),
                music.getAlbum(),
                music.getGenre(),
                music.getLyrics(),
                music.isLiked(),
                music.getLikeCount(),
                music.getCommentCount()
        );
    }

    public static MusicPageDTO entityToPageDTO(Music music){
        return new MusicPageDTO(
                music.getId(),
                music.getTitle(),
                music.getArtist().getName(),
                music.isLiked(),
                music.getLikeCount(),
                music.getCommentCount()
        );
    }
}
