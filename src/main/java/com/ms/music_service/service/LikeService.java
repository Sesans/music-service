package com.ms.music_service.service;

import com.ms.music_service.dto.LikeRequestDTO;

import java.util.UUID;

public interface LikeService {
    void likeMusic(LikeRequestDTO likeRequest);
}
