package com.ms.music_service.service.impl;

import com.ms.music_service.domain.Music;
import com.ms.music_service.dto.MusicRequestDTO;
import com.ms.music_service.dto.MusicResponseDTO;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    MusicRepository musicRepository;

    @Override
    public List<MusicResponseDTO> findAll() {
        return musicRepository.findAll()
                .stream()
                .map(music -> new MusicResponseDTO(
                        music.getId(),
                        music.getTitle(),
                        music.getCompositor(),
                        music.getAlbum(),
                        music.getGenre(),
                        music.getLyrics(),
                        music.getLikeCount(),
                        music.getCommentCount()
                ))
                .collect(Collectors.toList());
    }

    public void publishMusic(MusicRequestDTO musicRequest){
        Music newMusic = new Music();
        newMusic.setTitle(musicRequest.title());
        newMusic.setCompositor(musicRequest.compositor());
        newMusic.setAlbum(musicRequest.album());
        newMusic.setGenre(musicRequest.genre());
        newMusic.setLyrics(musicRequest.lyrics());
        newMusic.setLikeCount(0);
        newMusic.setCommentCount(0);
        musicRepository.save(newMusic);
    }
}