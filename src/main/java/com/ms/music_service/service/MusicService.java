package com.ms.music_service.service;

import com.ms.music_service.dto.MusicRequestDTO;
import com.ms.music_service.dto.MusicResponseDTO;

import java.util.List;

public interface MusicService {
    List<MusicResponseDTO> findAll();

    void publishMusic(MusicRequestDTO dto);
}
