package com.ms.music_service.controller;

import com.ms.music_service.dto.MusicRequestDTO;
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

    @GetMapping("/{musicId}")
    public ResponseEntity<?> getMusic(@PathVariable Long musicId){
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<MusicResponseDTO>> listMusics(){
        return ResponseEntity.status(200).body(musicService.findAll());
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publishMusic(@RequestBody MusicRequestDTO dto){
        musicService.publishMusic(dto);
        return ResponseEntity.status(200).build();
    }
}