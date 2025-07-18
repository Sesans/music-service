package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserComment;
import com.ms.music_service.dto.CommentRequestDTO;
import com.ms.music_service.dto.CommentResponseDTO;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.CommentRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.CommentService;
import com.ms.music_service.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    MusicRepository musicRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AuthUtil authUtil;

    @Override
    public List<CommentResponseDTO> listComments(Long musicId) {
        if(!musicRepository.existsById(musicId))
            throw new ResourceNotFoundException("There isn't a song with this ID: " + musicId);

        return commentRepository.findByMusicId(musicId)
                .stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getMusicId(),
                        comment.getUserId(),
                        comment.getComment()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentResponseDTO submitComment(Long musicId, CommentRequestDTO requestDTO) {
        if(!musicRepository.existsById(musicId))
            throw new ResourceNotFoundException("There isn't a song with this ID: " + musicId);

        UserComment comment = new UserComment();
        comment.setComment(requestDTO.comment());
        comment.setMusicId(musicId);
        comment.setUserId(authUtil.getCurrentUserId());
        commentRepository.save(comment);
        musicRepository.incrementCommentCount(musicId);
        return  new CommentResponseDTO(comment.getId(), comment.getMusicId(), comment.getUserId(), comment.getComment());
    }
}