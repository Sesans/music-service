package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserLike;
import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.dto.LikeResponseDTO;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.LikeRepository;
import com.ms.music_service.repository.MusicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {
    @Mock
    LikeRepository likeRepository;
    @Mock
    MusicRepository musicRepository;
    @InjectMocks
    LikeServiceImpl likeService;

    private UserLike like;
    private LikeRequestDTO requestDTO;

    @BeforeEach
    void setUp(){
        requestDTO = new LikeRequestDTO(UUID.randomUUID(), 1L);

        like = new UserLike();
        like.setId(1L);
        like.setUserId(requestDTO.userId());
        like.setMusicId(requestDTO.musicId());
        like.setTimestamp(LocalDateTime.now());
    }

    @Test
    void likeMusic_ShouldSetLikeSuccessfully(){
        when(musicRepository.existsById(requestDTO.musicId())).thenReturn(true);
        when(likeRepository.existsByUserId(requestDTO.userId())).thenReturn(false);
        when(likeRepository.save(any(UserLike.class))).thenReturn(like);

        LikeResponseDTO response = likeService.likeMusic(requestDTO);

        assertNotNull(response);
        assertEquals(requestDTO.userId(), response.userId());
        assertEquals(requestDTO.musicId(), response.musicId());
        assertNotNull(response.timestamp());

        verify(likeRepository, times(1)).save(any(UserLike.class));
        verify(musicRepository, times(1)).incrementLikeCount(requestDTO.musicId());
    }

    @Test
    void likeMusic_UserAlreadyLiked(){
        when(musicRepository.existsById(requestDTO.musicId())).thenReturn(true);
        when(likeRepository.existsByUserId(requestDTO.userId())).thenReturn(true);

        LikeResponseDTO response = likeService.likeMusic(requestDTO);

        assertNull(response.likeId());
        assertNull(response.userId());
        assertNull(response.musicId());
        assertNull(response.timestamp());

        verify(likeRepository, never()).save(any());
        verify(musicRepository, never()).incrementLikeCount(any());
    }

    @Test
    void likeMusic_ShouldThrowSongNotFound(){
        when(musicRepository.existsById(requestDTO.musicId())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> likeService.likeMusic(requestDTO));

        assertEquals("There isn't a song with this ID: " + requestDTO.musicId(), exception.getMessage());
        verify(likeRepository, never()).save(any());
        verify(musicRepository, never()).incrementLikeCount(any());
    }

    @Test
    void dislikeMusic_ShouldDeleteLikeSuccessfully(){
        when(musicRepository.existsById(requestDTO.musicId())).thenReturn(true);
        when(likeRepository.findByUserIdAndMusicId(requestDTO.userId(), requestDTO.musicId())).thenReturn(like);

        likeService.dislikeMusic(requestDTO);

        verify(likeRepository, times(1)).delete(any());
        verify(musicRepository, times(1)).decrementLikeCount(any());
    }

    @Test
    void dislikeMusic_UserDidNotLikedTheSong(){
        when(musicRepository.existsById(requestDTO.musicId())).thenReturn(true);
        when(likeRepository.findByUserIdAndMusicId(any(UUID.class), anyLong())).thenReturn(null);

        likeService.dislikeMusic(requestDTO);

        verify(likeRepository, never()).delete(any());
        verify(musicRepository, never()).decrementLikeCount(any());
    }
}