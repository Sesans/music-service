package com.ms.music_service.service;

import com.ms.music_service.dto.music.*;

import java.util.List;

public interface MusicService {
    List<MusicResponseDTO> findAll();

    MusicResponseDTO saveMusic(MusicRequestDTO dto);

    PagedResponseDTO pageList(int page, int size, String direction, SearchSort sortBy);

    List<MusicSuggestionDTO> autoComplete(String query);

    MusicResponseDTO getSong(Long musicId);
}
