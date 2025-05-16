package com.ms.music_service.controller;

import com.ms.music_service.dto.LikeRequestDTO;
import com.ms.music_service.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/like")
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST})
public class LikeController {
    @Autowired
    LikeService likeService;

    @PostMapping
    public ResponseEntity<?> submitLike(@RequestBody LikeRequestDTO likeRequest){
        likeService.likeMusic(likeRequest);
        return ResponseEntity.ok().build();
    }
}