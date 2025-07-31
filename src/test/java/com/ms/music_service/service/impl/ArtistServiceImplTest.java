package com.ms.music_service.service.impl;

import com.ms.music_service.domain.Artist;
import com.ms.music_service.dto.ArtistRequestDTO;
import com.ms.music_service.dto.ArtistResponseDTO;
import com.ms.music_service.exception.ConflictException;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceImplTest {
    @Mock
    ArtistRepository artistRepository;
    @InjectMocks
    ArtistServiceImpl artistService;

    private Artist artist;
    private ArtistRequestDTO requestDTO;

    @BeforeEach
    void setUp(){
        requestDTO = new ArtistRequestDTO("artist name");
        artist = new Artist();
        artist.setId(1L);
        artist.setName(requestDTO.name());
    }

    @Test
    void findAll_ShouldListArtistResponseDTO(){
        when(artistRepository.findAll()).thenReturn(List.of(artist));

        List<ArtistResponseDTO> response = artistService.findAll();

        assertNotNull(response);
        assertEquals(artist.getId(), response.get(0).id());
        assertEquals(artist.getName(), response.get(0).name());
    }

    @Test
    void getArtist_ShouldReturnSuccessfully(){
        when(artistRepository.findById(anyLong())).thenReturn(Optional.ofNullable(artist));

        ArtistResponseDTO response = artistService.getArtist(artist.getId());

        assertNotNull(response);
        assertEquals(artist.getId(), response.id());
        assertEquals(artist.getName(), response.name());
    }

    @Test
    void getArtist_ShouldThrowNotFoundException(){
        when(artistRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->
                artistService.getArtist(artist.getId())
        );
        assertEquals("Artist not found with ID: " + artist.getId(), exception.getMessage());
    }

    @Test
    void saveArtist_ShouldSaveSuccessfully(){
        when(artistRepository.existsByName(anyString())).thenReturn(false);

        ArtistResponseDTO response = artistService.saveArtist(requestDTO);

        assertNotNull(response);
        assertEquals(artist.getName(), response.name());
    }

    @Test
    void saveArtist_ArtistsAlreadySaved_ThrowConflictException(){
        when(artistRepository.existsByName(anyString())).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, ()->
                artistService.saveArtist(requestDTO)
        );
        assertEquals("Already have an artist/band registered with this name: " + requestDTO.name(), exception.getMessage());
    }
}