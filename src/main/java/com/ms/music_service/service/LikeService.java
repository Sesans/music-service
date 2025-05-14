package com.ms.music_service.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface LikeService {
    void likeMusic(UUID userId, Integer musicId);
}
