package com.ms.music_service.controller;

import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.dto.LikeResponseDTO;
import com.ms.music_service.service.LikeService;
import com.ms.music_service.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/musics")
@RestController
public class LikeController {
    @Autowired
    AuthUtil authUtil;
    @Autowired
    LikeService likeService;

    @PostMapping("/{musicId}/like")
    public ResponseEntity<LikeResponseDTO> submitLike(@PathVariable Long musicId){
        LikeRequestDTO likeRequest = new LikeRequestDTO(authUtil.getCurrentUser().getUserId(), musicId);
        LikeResponseDTO responseDTO = likeService.likeMusic(likeRequest);

        URI location = URI.create("/musics/" + responseDTO.likeId() + "/like");
        return ResponseEntity.created(location).body(responseDTO);
    }

    @DeleteMapping("/{musicId}/like")
    public ResponseEntity<Void> removeLike(@PathVariable Long musicId){
        LikeRequestDTO likeRequest = new LikeRequestDTO(authUtil.getCurrentUser().getUserId(), musicId);
        likeService.dislikeMusic(likeRequest);
        return ResponseEntity.noContent().build();
    }
}