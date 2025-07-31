package com.ms.music_service.controller;

import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.dto.LikeResponseDTO;
import com.ms.music_service.security.CustomUserDetails;
import com.ms.music_service.security.TokenService;
import com.ms.music_service.service.LikeService;
import com.ms.music_service.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
@AutoConfigureMockMvc
class LikeControllerTest{
    @Autowired
    MockMvc mockMvc;
    @MockBean
    LikeService likeService;
    @MockBean
    AuthUtil authUtil;
    @MockBean
    TokenService tokenService;

    private LikeResponseDTO responseDTO;
    private UUID userId;
    private LocalDateTime timestamp;
    private Long musicId;

    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();
        timestamp = LocalDateTime.now();
        musicId = 1L;
        responseDTO = new LikeResponseDTO(1L, userId, musicId, timestamp);
    }

    @Test
    void submitLike_ShouldReturnCreatedWithJson() throws Exception {
        when(likeService.likeMusic(any(LikeRequestDTO.class))).thenReturn(responseDTO);
        when(authUtil.getCurrentUserId()).thenReturn(userId);
        when(authUtil.getCurrentUser()).thenReturn(new CustomUserDetails(userId, "admin"));

        mockMvc.perform(post("/musics/{musicId}/like", musicId)
                    .with(user("admin").roles("ADMIN"))
                    .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likeId").value(1L))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.musicId").value(musicId))
                .andExpect(jsonPath("$.timestamp").value(timestamp.toString()));
    }

    @Test
    void removeLike_ShouldReturnNoContent() throws Exception {
        when(authUtil.getCurrentUserId()).thenReturn(userId);
        when(authUtil.getCurrentUser()).thenReturn(new CustomUserDetails(userId, "admin"));

        mockMvc.perform(delete("/musics/{musicId}/like", musicId)
                .with(user("admin").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}