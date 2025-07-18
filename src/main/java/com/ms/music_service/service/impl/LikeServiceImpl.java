package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserLike;
import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.dto.LikeResponseDTO;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.LikeRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    MusicRepository musicRepository;

    @Transactional
    @Override
    public LikeResponseDTO likeMusic(LikeRequestDTO likeRequest){
        if(!musicRepository.existsById(likeRequest.musicId()))
            throw new ResourceNotFoundException("There isn't a song with this ID: " + likeRequest.musicId());

        UserLike like = new UserLike();
        if(!likeRepository.existsByUserId(likeRequest.userId())){
            like.setTimestamp(LocalDateTime.now());
            like.setUserId(likeRequest.userId());
            like.setMusicId(likeRequest.musicId());
            likeRepository.save(like);
            musicRepository.incrementLikeCount(likeRequest.musicId());
        }

        return new LikeResponseDTO(like.getId(), like.getUserId(), like.getMusicId(), like.getTimestamp());
    }

    @Transactional
    @Override
    public void dislikeMusic(LikeRequestDTO likeRequest) {
        UserLike like = likeRepository.findByUserIdAndMusicId(likeRequest.userId(), likeRequest.musicId());
        if(like != null) {
            likeRepository.delete(like);
            musicRepository.decrementLikeCount(likeRequest.musicId());
        }
    }
}
