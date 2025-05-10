package com.ms.music_service.controller;

import com.ms.music_service.dto.MusicResponseDTO;
import com.ms.music_service.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/music")
@RestController
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class MusicController {
    @Autowired
    MusicService musicService;

    @GetMapping("/list")
    public ResponseEntity<List<MusicResponseDTO>> listMusics(){
        return ResponseEntity.status(200).body(musicService.findAll());
    }
}