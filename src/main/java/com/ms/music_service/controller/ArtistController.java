package com.ms.music_service.controller;

import com.ms.music_service.dto.ArtistRequestDTO;
import com.ms.music_service.dto.ArtistResponseDTO;
import com.ms.music_service.service.ArtistService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @RolesAllowed("ADMIN")
    @PostMapping("/save")
    public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody ArtistRequestDTO dto){
        ArtistResponseDTO responseDTO = artistService.saveArtist(dto);
        URI location = URI.create("/artists/" + responseDTO.id());

        return ResponseEntity.created(location).body(responseDTO);
    }
}
