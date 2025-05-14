package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserLike;
import com.ms.music_service.repository.LikeRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    MusicRepository musicRepository;

    @Transactional
    @Override
    public void likeMusic(UUID userId, Integer musicId){
        if(!likeRepository.existsByUserIdAndMusicId(userId, musicId)){
            UserLike like = new UserLike();
            like.setTimestamp(LocalDateTime.now());
            like.setUserId(userId);
            like.setMusicId(musicId);
            likeRepository.save(like);
            musicRepository.incrementLikeCount(musicId);
        }
    }
}
