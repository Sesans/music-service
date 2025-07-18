package com.ms.music_service.service;

import com.ms.music_service.dto.CommentRequestDTO;
import com.ms.music_service.dto.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    List<CommentResponseDTO> listComments(Long musicId);

    CommentResponseDTO submitComment(Long musicId, CommentRequestDTO dto);
}