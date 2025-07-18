package com.ms.music_service.controller;

import com.ms.music_service.dto.music.*;
import com.ms.music_service.service.MusicService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/musics")
@RestController
public class MusicController {
    @Autowired
    MusicService musicService;

    @GetMapping("/{musicId}")
    public ResponseEntity<MusicResponseDTO> getMusic(@PathVariable Long musicId){
        return ResponseEntity.ok(musicService.getSong(musicId));
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
    @GetMapping("/admin/list")
    public ResponseEntity<List<MusicResponseDTO>> listMusics(){
        return ResponseEntity.ok().body(musicService.findAll());
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/admin/publish")
    public ResponseEntity<MusicResponseDTO> publishMusic(@RequestBody MusicRequestDTO dto){
        MusicResponseDTO responseDTO = musicService.saveMusic(dto);
        URI location = URI.create("/musics/" + responseDTO.id());

        return ResponseEntity.created(location).body(responseDTO);
    }
}