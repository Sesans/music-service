package com.ms.music_service.service;

import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.dto.LikeResponseDTO;

public interface LikeService {
    LikeResponseDTO likeMusic(LikeRequestDTO likeRequest);

    void dislikeMusic(LikeRequestDTO likeRequest);
}
