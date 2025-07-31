package com.ms.music_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.music_service.dto.CommentRequestDTO;
import com.ms.music_service.dto.CommentResponseDTO;
import com.ms.music_service.security.TokenService;
import com.ms.music_service.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CommentService commentService;
    @MockBean
    TokenService tokenService;

    private Long musicId;
    private CommentResponseDTO responseDTO;
    private CommentRequestDTO requestDTO;
    private UUID userId;
    @BeforeEach
    void setUp(){
        musicId = 1L;
        userId = UUID.randomUUID();
        responseDTO = new CommentResponseDTO(1L, musicId, userId, "This is a comment example");
        requestDTO = new CommentRequestDTO("This is a comment example");
    }

    @Test
    void getComments_ShouldReturnOkWithResponseList() throws Exception {
        when(commentService.listComments(anyLong())).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/musics/{musicId}/comments", musicId)
                .with(user("admin").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].commentId").value(1L))
                .andExpect(jsonPath("$[0].musicId").value(musicId))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].comment").value("This is a comment example"));
    }

    @Test
    void publishComment_ShouldReturnCreatedWithJson() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        when(commentService.submitComment(anyLong(), any(CommentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/musics/{musicId}/comments", musicId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.musicId").value(musicId))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.comment").value("This is a comment example"));
    }
}