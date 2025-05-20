package com.ms.music_service.service;

import com.ms.music_service.dto.LikeRequestDTO;

public interface LikeService {
    void likeMusic(LikeRequestDTO likeRequest);

    void dislikeMusic(LikeRequestDTO likeRequest);
}
