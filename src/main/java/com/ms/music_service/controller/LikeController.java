package com.ms.music_service.controller;

import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/likes")
@RestController
public class LikeController {
    @Autowired
    AuthUtil authUtil;
    @Autowired
    LikeService likeService;

    @PostMapping("/{musicId}")
    public ResponseEntity<?> submitLike(@PathVariable Long musicId, Authentication authentication){
        LikeRequestDTO likeRequest = new LikeRequestDTO(authUtil.getCurrentUser().getUserId(), musicId);
        likeService.likeMusic(likeRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{musicId}")
    public ResponseEntity<?> removeLike(@PathVariable Long musicId, Authentication authentication){
        LikeRequestDTO likeRequest = new LikeRequestDTO(authUtil.getCurrentUser().getUserId(), musicId);
        likeService.dislikeMusic(likeRequest);
        return ResponseEntity.noContent().build();
    }
}