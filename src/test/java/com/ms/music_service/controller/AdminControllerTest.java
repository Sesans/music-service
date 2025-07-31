package com.ms.music_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.music_service.dto.music.*;
import com.ms.music_service.security.TokenService;
import com.ms.music_service.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MusicService musicService;
    @MockBean
    TokenService tokenService;

    private MusicResponseDTO responseDTO;
    private MusicRequestDTO requestDTO;

    @BeforeEach
    void setUp(){
        Long musicId = 1L;
        requestDTO = new MusicRequestDTO("Song title", 1L, "Request Album", "Request Genre", "Request Lyrics");
        responseDTO = new MusicResponseDTO(musicId, "Title", "Artist", "Album", "Rock", "Lyrics", false, 0, 0);
    }

    @Test
    void listMusics_ShouldReturnOkWithJson() throws Exception{
        List<MusicResponseDTO> mockList = List.of(responseDTO);
        when(musicService.findAll()).thenReturn(mockList);

        mockMvc.perform(get("/admin/list")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void publishMusic_ShouldReturnCreatedWithJson() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        when(musicService.saveMusic(any(MusicRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/admin/publish")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"));
    }
}