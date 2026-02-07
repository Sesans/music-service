package com.ms.music_service.controller;

import com.ms.music_service.dto.ArtistResponseDTO;
import com.ms.music_service.security.TokenService;
import com.ms.music_service.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
@AutoConfigureMockMvc
class ArtistControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ArtistService artistService;
    @MockBean
    TokenService tokenService;

    private ArtistResponseDTO responseDTO;
    private Long artistId;

    @BeforeEach
    void setUp(){
        artistId = 1L;
        responseDTO = new ArtistResponseDTO(artistId, "Artist name");
    }

    @Test
    void listArtist_ShouldReturnOkWithJson() throws Exception {
        when(artistService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/artists/list")
                    .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Artist name"));
    }

    @Test
    void getArtist_ShouldReturnOkWithJson() throws Exception {
        when(artistService.getArtist(anyLong())).thenReturn(responseDTO);

        mockMvc.perform(get("/artists/{artistId}", artistId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Artist name"));
    }
}