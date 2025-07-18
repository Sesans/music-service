package com.ms.music_service.service;

import com.ms.music_service.dto.ArtistRequestDTO;
import com.ms.music_service.dto.ArtistResponseDTO;

import java.util.List;

public interface ArtistService {
    List<ArtistResponseDTO> findAll();

    ArtistResponseDTO saveArtist(ArtistRequestDTO dto);

    ArtistResponseDTO getArtist(Long artistId);
}
