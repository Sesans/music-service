package com.ms.music_service.service.impl;

import com.ms.music_service.domain.Music;
import com.ms.music_service.domain.UserAnnotation;
import com.ms.music_service.dto.AnnotationRequestDTO;
import com.ms.music_service.dto.AnnotationResponseDTO;
import com.ms.music_service.exception.InvalidAnnotationException;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.AnnotationRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnotationServiceImplTest {
    @Mock
    AnnotationRepository annotationRepository;
    @Mock
    MusicRepository musicRepository;
    @Mock
    AuthUtil authUtil;

    @InjectMocks
    AnnotationServiceImpl annotationService;

    private UserAnnotation annotation;
    private UserAnnotation annotation2;
    private Music music;
    private AnnotationRequestDTO requestDTO;
    private AnnotationRequestDTO requestDTO2;

    @BeforeEach
    void setUp(){
        music = new Music();
        music.setId(1L);
        music.setLyrics("This is the full example. This is a excerpt example, chill out. This is a excerpt example, keep going.");

        requestDTO = new AnnotationRequestDTO("Note A", "This is a excerpt example, chill out.", 26, 63);
        requestDTO2 = new AnnotationRequestDTO("Note B", "This is a excerpt example, chill out.", 26, 61);

        annotation = new UserAnnotation(
                1L,
                UUID.randomUUID(),
                1L,
                requestDTO.note(),
                requestDTO.excerpt(),
                requestDTO.startIndex(),
                requestDTO.endIndex(),
                LocalDateTime.now()
        );

        annotation2 = new UserAnnotation(
                2L,
                UUID.randomUUID(),
                1L,
                "Note B",
                "This is a excerpt example, keep going.",
                64,
                101,
                LocalDateTime.now()
        );
    }

    @Test
    void findAll_ShouldReturnListOfAnnotationResponseDTO(){
        when(musicRepository.existsById(anyLong())).thenReturn(true);
        when(annotationRepository.findAllByMusicId(anyLong())).thenReturn(List.of(annotation, annotation2));

        List<AnnotationResponseDTO> response = annotationService.findAll(1L);

        assertNotNull(response);
        assertEquals(annotation.getId(), response.get(0).annotationId());
        assertEquals(annotation2.getId(), response.get(1).annotationId());

        verify(annotationRepository, times(1)).findAllByMusicId(anyLong());
    }

    @Test
    void findAll_ShouldThrowNotFoundException(){
        when(musicRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->
                annotationService.findAll(music.getId())
        );
        assertEquals("There isn't a song with this ID: " + music.getId(), exception.getMessage());
    }

    @Test
    void submitAnnotation_ShouldSubmitSuccessfully(){
        when(musicRepository.findById(anyLong())).thenReturn(Optional.ofNullable(music));
        when(annotationRepository.save(any(UserAnnotation.class))).thenReturn(annotation);
        when(authUtil.getCurrentUserId()).thenReturn(annotation.getUserId());

        AnnotationResponseDTO response = annotationService.submitAnnotation(music.getId(), requestDTO);

        assertNotNull(response);
        assertEquals(annotation.getUserId(), response.userId());
        assertEquals(annotation.getMusicId(), response.musicId());
        assertEquals(annotation.getNote(), response.note());
        assertEquals(annotation.getExcerpt(), response.excerpt());
        assertEquals(annotation.getStartIndex(), response.startIndex());
        assertEquals(annotation.getEndIndex(), response.endIndex());
    }

    @Test
    void submitAnnotation_ShouldThrowInvalidAnnotationException(){
        when(musicRepository.findById(anyLong())).thenReturn(Optional.ofNullable(music));

        InvalidAnnotationException exception = assertThrows(InvalidAnnotationException.class, () ->
                annotationService.submitAnnotation(music.getId(), requestDTO2)
        );

        assertEquals("Invalid excerpt", exception.getMessage());
    }

    @Test
    void submitAnnotation_ShouldThrowNotFoundException(){
        when(musicRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->
                annotationService.submitAnnotation(music.getId(), requestDTO)
        );

        assertEquals("There isn't a song with this ID: " + music.getId(), exception.getMessage());
    }
}