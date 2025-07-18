package com.ms.music_service.service.impl;

import com.ms.music_service.domain.Artist;
import com.ms.music_service.dto.ArtistRequestDTO;
import com.ms.music_service.dto.ArtistResponseDTO;
import com.ms.music_service.exception.ConflictException;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.ArtistRepository;
import com.ms.music_service.service.ArtistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    ArtistRepository artistRepository;

    @Override
    public List<ArtistResponseDTO> findAll() {
        List<Artist> artistList = artistRepository.findAll();

        return artistList.stream().map(artist -> new ArtistResponseDTO(
                artist.getId(),
                artist.getName()
        )).toList();
    }

    @Override
    public ArtistResponseDTO getArtist(Long artistId) {
        return artistRepository.findById(artistId).map(artist -> new ArtistResponseDTO(
                artist.getId(),
                artist.getName()
        )).orElseThrow(() -> new ResourceNotFoundException("Artist not found with ID: " + artistId));
    }

    @Transactional
    @Override
    public ArtistResponseDTO saveArtist(ArtistRequestDTO dto) {
        if(artistRepository.existsByName(dto.name()))
            throw new ConflictException("Already have an artist/band registered with this name: " + dto.name());

        Artist artist = new Artist();
        artist.setName(dto.name());
        artistRepository.save(artist);
        return new ArtistResponseDTO(artist.getId(), artist.getName());
    }
}
