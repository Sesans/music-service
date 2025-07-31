package com.ms.music_service.controller;

import com.ms.music_service.dto.CommentResponseDTO;
import com.ms.music_service.dto.CommentRequestDTO;
import com.ms.music_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/musics")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/{musicId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Long musicId){
        return ResponseEntity.ok().body(commentService.listComments(musicId));
    }

    @PostMapping("/{musicId}/comments")
    public ResponseEntity<CommentResponseDTO> publishComment(@PathVariable Long musicId, @RequestBody CommentRequestDTO requestDTO){
        CommentResponseDTO responseDTO = commentService.submitComment(musicId, requestDTO);
        URI location = URI.create("/comments/" + responseDTO.commentId());

        return ResponseEntity.created(location).body(responseDTO);
    }
}