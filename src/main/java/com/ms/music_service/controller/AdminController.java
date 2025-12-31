package com.ms.music_service.controller;

import com.ms.music_service.dto.ArtistRequestDTO;
import com.ms.music_service.dto.ArtistResponseDTO;
import com.ms.music_service.dto.music.MusicRequestDTO;
import com.ms.music_service.dto.music.MusicResponseDTO;
import com.ms.music_service.service.ArtistService;
import com.ms.music_service.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    MusicService musicService;
    @Autowired
    ArtistService artistService;

    @GetMapping("/list")
    public ResponseEntity<List<MusicResponseDTO>> listMusics(){
        return ResponseEntity.ok().body(musicService.findAll());
    }

    @PostMapping("/publish/music")
    public ResponseEntity<MusicResponseDTO> publishMusic(@RequestBody MusicRequestDTO dto){
        MusicResponseDTO responseDTO = musicService.saveMusic(dto);
        URI location = URI.create("/musics/" + responseDTO.id());

        return ResponseEntity.created(location).body(responseDTO);
    }

    @PostMapping("/publish/artist")
    public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody ArtistRequestDTO dto){
        ArtistResponseDTO responseDTO = artistService.saveArtist(dto);
        URI location = URI.create("/artists/" + responseDTO.id());

        return ResponseEntity.created(location).body(responseDTO);
    }
}
