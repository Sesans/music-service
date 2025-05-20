package com.ms.music_service.service;

import com.ms.music_service.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> listComments(Long musicId);

    void submitComment(CommentDTO dto);
}