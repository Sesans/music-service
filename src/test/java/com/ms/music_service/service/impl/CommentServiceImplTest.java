package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserComment;
import com.ms.music_service.dto.CommentRequestDTO;
import com.ms.music_service.dto.CommentResponseDTO;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.CommentRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    MusicRepository musicRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    AuthUtil authUtil;
    @InjectMocks
    CommentServiceImpl commentService;

    private UserComment userComment;
    private CommentRequestDTO requestDTO;
    private Long musicId;

    @BeforeEach
    void setUp(){
        musicId = 1L;
        requestDTO = new CommentRequestDTO("This is the comment example");
        userComment = new UserComment();
        userComment.setId(1L);
        userComment.setMusicId(musicId);
        userComment.setUserId(UUID.randomUUID());
        userComment.setComment(requestDTO.comment());
    }

    @Test
    void listComments_ShouldListCommentResponseDTO(){
        when(musicRepository.existsById(anyLong())).thenReturn(true);
        when(commentRepository.findByMusicId(anyLong())).thenReturn(List.of(userComment));

        List<CommentResponseDTO> response = commentService.listComments(musicId);

        assertNotNull(response);
        assertEquals(userComment.getId(), response.get(0).commentId());
        assertEquals(userComment.getMusicId(), response.get(0).musicId());
        assertEquals(userComment.getUserId(), response.get(0).userId());
        assertEquals(userComment.getComment(), response.get(0).comment());

        verify(commentRepository, times(1)).findByMusicId(any());
    }

    @Test
    void listComment_ShouldThrowNotFoundException(){
        when(musicRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->
                commentService.listComments(musicId)
        );
        assertEquals("There isn't a song with this ID: " + musicId, exception.getMessage());
    }

    @Test
    void submitComment_ShouldSubmitSuccessfully(){
        when(musicRepository.existsById(anyLong())).thenReturn(true);
        when(authUtil.getCurrentUserId()).thenReturn(userComment.getUserId());

        CommentResponseDTO response = commentService.submitComment(musicId, requestDTO);

        assertEquals(musicId, response.musicId());
        assertEquals(userComment.getUserId(), response.userId());
        assertEquals(userComment.getComment(), response.comment());
    }

    @Test
    void submitComment_ShouldThrowNotFoundException(){
        when(musicRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->
                commentService.submitComment(musicId, requestDTO)
        );
        assertEquals("There isn't a song with this ID: " + musicId, exception.getMessage());
    }
}