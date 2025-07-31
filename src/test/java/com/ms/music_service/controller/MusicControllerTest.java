package com.ms.music_service.controller;

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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MusicController.class)
@AutoConfigureMockMvc
class MusicControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MusicService musicService;
    @MockBean
    TokenService tokenService;

    private MusicResponseDTO responseDTO;
    private PagedResponseDTO pagedResponseDTO;
    private Long musicId;
    private MusicSuggestionDTO suggestionDTO;

    @BeforeEach
    void setUp(){
        musicId = 1L;
        MusicPageDTO pageDTO;
        responseDTO = new MusicResponseDTO(musicId, "Title", "Artist", "Album", "Rock", "Lyrics", false, 0, 0);
        pageDTO = new MusicPageDTO(musicId, "Page title", "Page artist", true, 10, 10);
        pagedResponseDTO = new PagedResponseDTO(List.of(pageDTO), false);
        suggestionDTO = new MusicSuggestionDTO(musicId, "Suggestion title", "Suggestion artist");
    }


    @Test
    void getMusic_ShouldReturnOkWithJson() throws Exception {
        when(musicService.getSong(anyLong())).thenReturn(responseDTO);

        mockMvc.perform(get("/musics/{musicId}", musicId)
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.artist").value("Artist"));
    }

    @Test
    void getMusicPageable_ShouldReturnOkWithJson() throws Exception {
        when(musicService.pageList(0, 10, "DESC", SearchSort.LIKE_COUNT)).thenReturn(pagedResponseDTO);

        mockMvc.perform(get("/musics/search")
                        .with(user("user").roles("USER"))
                    .param("page", "0")
                    .param("size", "10")
                    .param("sortBy", "LIKE_COUNT")
                    .param("direction", "DESC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void autoCompleteUserQuery_ShouldReturnOkWithJson() throws Exception {
        when(musicService.autoComplete(anyString())).thenReturn(List.of(suggestionDTO));

        mockMvc.perform(get("/musics/autocomplete")
                .with(user("user").roles("USER"))
                .param("query", "Suggestion title"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Suggestion title"))
                .andExpect(jsonPath("$[0].artist").value("Suggestion artist"));
    }
}