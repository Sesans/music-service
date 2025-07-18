package com.ms.music_service.controller;

import com.ms.music_service.dto.AnnotationRequestDTO;
import com.ms.music_service.dto.AnnotationResponseDTO;
import com.ms.music_service.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/musics")
public class AnnotationController {
    @Autowired
    AnnotationService annotationService;

    @GetMapping("/{musicId}/annotations")
    public ResponseEntity<List<AnnotationResponseDTO>> findAll(@PathVariable Long musicId){
        return ResponseEntity.ok(annotationService.findAll(musicId));
    }

    @PostMapping("/{musicId}/annotations")
    public ResponseEntity<AnnotationResponseDTO> publishAnnotation(@PathVariable Long musicId, @RequestBody AnnotationRequestDTO requestDTO){
        AnnotationResponseDTO responseDTO = annotationService.submitAnnotation(musicId, requestDTO);
        URI location = URI.create("/annotations/" + responseDTO.annotationId());

        return ResponseEntity.created(location).body(responseDTO);
    }
}
