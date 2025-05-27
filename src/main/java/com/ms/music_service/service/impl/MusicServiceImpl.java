package com.ms.music_service.service.impl;

import com.ms.music_service.controller.AuthUtil;
import com.ms.music_service.domain.Music;
import com.ms.music_service.dto.*;
import com.ms.music_service.repository.LikeRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    MusicRepository musicRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    AuthUtil authUtil;

    @Override
    public PagedResponseDTO pageList(int page, int size, String direction, SearchSort sortBy) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy.getField());
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Music> musics = musicRepository.findAll(pageable);
        setLikes(musics);

        List<MusicPageDTO> musicList = musics.map(music -> new MusicPageDTO(
                music.getId(),
                music.getTitle(),
                music.getArtist(),
                music.getLikeCount(),
                music.getCommentCount()
                )).stream().toList();
        return new PagedResponseDTO(musicList, musics.hasNext());
    }

    @Override
    public List<MusicResponseDTO> findAll() {
        List<Music> musicList = musicRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));
        setLikes(musicList);

        return musicList.stream()
                .map( music -> new MusicResponseDTO(
                        music.getId(),
                        music.getTitle(),
                        music.getArtist(),
                        music.getAlbum(),
                        music.getLyrics(),
                        music.getGenre(),
                        music.isLiked(),
                        music.getLikeCount(),
                        music.getCommentCount()
                )).collect(Collectors.toList());
    }

    public void saveMusic(MusicRequestDTO musicRequest){
        Music newMusic = new Music();
        newMusic.setTitle(musicRequest.title());
        newMusic.setArtist(musicRequest.artist());
        newMusic.setAlbum(musicRequest.album());
        newMusic.setGenre(musicRequest.genre());
        newMusic.setLyrics(musicRequest.lyrics());
        newMusic.setLikeCount(0);
        newMusic.setCommentCount(0);
        musicRepository.save(newMusic);
    }

    public void setLikes(Iterable<Music> musics){
        if (authUtil.isAuthenticated()){
            for(Music music : musics){
                boolean liked = likeRepository.existsByUserIdAndMusicId(authUtil.getCurrentUserId(), music.getId());
                music.setLiked(liked);
            }
        }
    }
}