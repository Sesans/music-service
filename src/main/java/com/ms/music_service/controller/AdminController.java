package com.ms.music_service.controller;

import com.ms.music_service.dto.music.MusicRequestDTO;
import com.ms.music_service.dto.music.MusicResponseDTO;
import com.ms.music_service.service.MusicService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RolesAllowed("ADMIN")
public class AdminController {
    @Autowired
    MusicService musicService;

    @GetMapping("/list")
    public ResponseEntity<List<MusicResponseDTO>> listMusics(){
        return ResponseEntity.ok().body(musicService.findAll());
    }

    @PostMapping("/publish")
    public ResponseEntity<MusicResponseDTO> publishMusic(@RequestBody MusicRequestDTO dto){
        MusicResponseDTO responseDTO = musicService.saveMusic(dto);
        URI location = URI.create("/musics/" + responseDTO.id());

        return ResponseEntity.created(location).body(responseDTO);
    }
}
