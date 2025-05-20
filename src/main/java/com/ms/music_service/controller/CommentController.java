package com.ms.music_service.controller;

import com.ms.music_service.dto.CommentDTO;
import com.ms.music_service.dto.CommentRequestDTO;
import com.ms.music_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CommentController {
    @Autowired
    AuthUtil authUtil;
    @Autowired
    CommentService commentService;

    @GetMapping("/{musicId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long musicId){
        return ResponseEntity.ok().body(commentService.listComments(musicId));
    }

    @PostMapping("/{musicId}")
    public ResponseEntity<?> submitComment(@PathVariable Long musicId, Authentication authentication, @RequestBody CommentRequestDTO request){
        CommentDTO commentDTO = new CommentDTO(musicId, authUtil.getCurrentUser().getUserId(), request.comment());
        commentService.submitComment(commentDTO);
        return ResponseEntity.ok().build();
    }
}