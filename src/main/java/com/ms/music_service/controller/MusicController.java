package com.ms.music_service.controller;

import com.ms.music_service.dto.*;
import com.ms.music_service.service.MusicService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/music")
@RestController
public class MusicController {
    @Autowired
    MusicService musicService;

    @GetMapping("/{musicId}")
    public ResponseEntity<?> getMusic(@PathVariable Long musicId){
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponseDTO> getMusicPageable(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "LIKE_COUNT") SearchSort sortBy,
            @RequestParam(defaultValue = "DESC")String direction){
        return ResponseEntity.ok().body(musicService.pageList(page, size, direction, sortBy));
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<MusicSuggestionDTO>> autoCompleteUserQuery(@RequestParam String query){
        return ResponseEntity.ok().body(musicService.autoComplete(query));
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/list")
    public ResponseEntity<List<MusicResponseDTO>> listMusics(){
        return ResponseEntity.status(200).body(musicService.findAll());
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/publish")
    public ResponseEntity<?> publishMusic(@RequestBody MusicRequestDTO dto){
        musicService.saveMusic(dto);
        return ResponseEntity.status(200).build();
    }
}