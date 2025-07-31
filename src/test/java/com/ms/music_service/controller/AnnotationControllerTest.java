package com.ms.music_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.music_service.dto.AnnotationRequestDTO;
import com.ms.music_service.dto.AnnotationResponseDTO;
import com.ms.music_service.security.TokenService;
import com.ms.music_service.service.AnnotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AnnotationController.class)
@AutoConfigureMockMvc
class AnnotationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AnnotationService annotationService;
    @MockBean
    TokenService tokenService;

    private AnnotationResponseDTO responseDTO;
    private AnnotationRequestDTO requestDTO;
    private UUID userId;
    private Long musicId;
    private LocalDateTime timestamp;
    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();
        musicId = 1L;
        timestamp = LocalDateTime.now();
        responseDTO = new AnnotationResponseDTO(1L, userId, musicId, "Note example", "Excerpt example", 1, 2, timestamp);
        requestDTO = new AnnotationRequestDTO("Note request example", "Excerpt request example", 1, 2);
    }

    @Test
    void findAll_ShouldReturnOkWithJson() throws Exception {
        when(annotationService.findAll(anyLong())).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/musics/{musicId}/annotations", musicId)
                    .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].annotationId").value(1L))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].musicId").value(musicId))
                .andExpect(jsonPath("$[0].note").value("Note example"))
                .andExpect(jsonPath("$[0].excerpt").value("Excerpt example"))
                .andExpect(jsonPath("$[0].startIndex").value(1))
                .andExpect(jsonPath("$[0].endIndex").value(2))
                .andExpect(jsonPath("$[0].timestamp").value(timestamp.toString()));
    }

    @Test
    void publishAnnotation_ShouldReturnCreatedWithJson() throws Exception{
        final ObjectMapper objectMapper = new ObjectMapper();
        when(annotationService.submitAnnotation(anyLong(), any(AnnotationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/musics/{musicId}/annotations", musicId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.annotationId").value(1L))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.musicId").value(musicId))
                .andExpect(jsonPath("$.note").value("Note example"))
                .andExpect(jsonPath("$.excerpt").value("Excerpt example"))
                .andExpect(jsonPath("$.startIndex").value(1))
                .andExpect(jsonPath("$.endIndex").value(2))
                .andExpect(jsonPath("$.timestamp").value(timestamp.toString()));
    }
}