package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserLike;
import com.ms.music_service.dto.LikeRequestDTO;
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
    public void likeMusic(LikeRequestDTO likeRequest){
        if(!likeRepository.existsByUserIdAndMusicId(likeRequest.userId(), likeRequest.musicId())){
            UserLike like = new UserLike();
            like.setTimestamp(LocalDateTime.now());
            like.setUserId(likeRequest.userId());
            like.setMusicId(likeRequest.musicId());
            likeRepository.save(like);
            musicRepository.incrementLikeCount(likeRequest.musicId());
        }
    }

    @Override
    public void dislikeMusic(LikeRequestDTO likeRequest) {
        UserLike like = likeRepository.findByUserIdAndMusicId(likeRequest.userId(), likeRequest.musicId());
        if(like != null) {
            likeRepository.delete(like);
            musicRepository.decrementLikeCount(likeRequest.musicId());
        }
    }
}
