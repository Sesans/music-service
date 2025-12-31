package com.ms.music_service.controller;

import com.ms.music_service.dto.ArtistResponseDTO;
import com.ms.music_service.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    @Autowired
    ArtistService artistService;

    @GetMapping("/list")
    public ResponseEntity<List<ArtistResponseDTO>> listArtist(){
        return ResponseEntity.ok(artistService.findAll());
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponseDTO> getArtist(@PathVariable Long artistId){
        return ResponseEntity.ok(artistService.getArtist(artistId));
    }
}
