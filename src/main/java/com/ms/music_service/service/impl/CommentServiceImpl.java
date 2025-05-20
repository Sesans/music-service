package com.ms.music_service.service.impl;

import com.ms.music_service.domain.UserComment;
import com.ms.music_service.dto.CommentDTO;
import com.ms.music_service.repository.CommentRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.CommentService;
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
    @Override
    public List<CommentDTO> listComments(Long musicId) {
        return commentRepository.findByMusicId(musicId)
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getMusicId(),
                        comment.getUserId(),
                        comment.getComment()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void submitComment(CommentDTO dto) {
        UserComment comment = new UserComment();
        comment.setComment(dto.comment());
        comment.setMusicId(dto.musicId());
        comment.setUserId(dto.userId());
        commentRepository.save(comment);
        musicRepository.incrementCommentCount(dto.musicId());
    }
}