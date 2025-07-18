package com.ms.music_service.service.impl;

import com.ms.music_service.util.AuthUtil;
import com.ms.music_service.domain.Artist;
import com.ms.music_service.domain.Music;
import com.ms.music_service.dto.music.*;
import com.ms.music_service.exception.ConflictException;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.ArtistRepository;
import com.ms.music_service.repository.LikeRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.MusicService;
import com.ms.music_service.util.MusicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    MusicRepository musicRepository;
    @Autowired
    ArtistRepository artistRepository;
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

        List<MusicPageDTO> musicList = musics.map(MusicMapper::entityToPageDTO).toList();
        return new PagedResponseDTO(musicList, musics.hasNext());
    }

    @Override
    public List<MusicSuggestionDTO> autoComplete(String query) {
        List<Music> musicList = musicRepository.findByTitleOrArtist(query);
        return musicList.stream().map(music -> new MusicSuggestionDTO(
                music.getId(),
                music.getTitle(),
                music.getArtist().getName()
        )).toList();
    }

    @Override
    public MusicResponseDTO getSong(Long musicId) {
        Music music = musicRepository.findById(musicId).orElseThrow(() -> new ResourceNotFoundException("Song not found with ID: " + musicId));
        setLikes(Collections.singleton(music));
        return MusicMapper.entityToResponseDTO(music);
    }

    @Override
    public List<MusicResponseDTO> findAll() {
        List<Music> musicList = musicRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));
        setLikes(musicList);

        return musicList.stream().map(MusicMapper::entityToResponseDTO).toList();
    }

    @Override
    @Transactional
    public MusicResponseDTO saveMusic(MusicRequestDTO musicRequest){
        Artist artist = artistRepository.findById(musicRequest.artistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with ID: " + musicRequest.artistId()));

        if(musicRepository.existsByTitleAndArtistId(musicRequest.title(), musicRequest.artistId())){
            throw new ConflictException("Artist already have this song registered.");
        }

        Music music = MusicMapper.requestToEntity(musicRequest, artist);
        musicRepository.save(music);
        return MusicMapper.entityToResponseDTO(music);
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