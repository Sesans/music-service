package com.ms.music_service.service.impl;

import com.ms.music_service.domain.Music;
import com.ms.music_service.domain.UserAnnotation;
import com.ms.music_service.dto.AnnotationRequestDTO;
import com.ms.music_service.dto.AnnotationResponseDTO;
import com.ms.music_service.exception.InvalidAnnotationException;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.AnnotationRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.service.AnnotationService;
import com.ms.music_service.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnotationServiceImpl implements AnnotationService {
    @Autowired
    AnnotationRepository annotationRepository;
    @Autowired
    MusicRepository musicRepository;
    @Autowired
    AuthUtil authUtil;

    @Override
    public List<AnnotationResponseDTO> findAll(Long musicId) {
        if(!musicRepository.existsById(musicId))
            throw new ResourceNotFoundException("There isn't a song with this ID: " + musicId);

        return annotationRepository.findAllByMusicId(musicId).stream().map(annotation -> new AnnotationResponseDTO(
                annotation.getId(),
                annotation.getUserId(),
                annotation.getMusicId(),
                annotation.getNote(),
                annotation.getExcerpt(),
                annotation.getStartIndex(),
                annotation.getEndIndex(),
                annotation.getTimestamp()
        )).toList();
    }

    @Transactional
    @Override
    public AnnotationResponseDTO submitAnnotation(Long musicId, AnnotationRequestDTO requestDTO) {
        Music music = musicRepository.findById(musicId).orElseThrow(() -> new ResourceNotFoundException("There isn't a song with this ID: " + musicId));
        String lyrics = music.getLyrics();
        if(!lyrics.substring(requestDTO.startIndex(), requestDTO.endIndex()).equals(requestDTO.excerpt()))
            throw new InvalidAnnotationException("Invalid excerpt");

        UserAnnotation userAnnotation = new UserAnnotation();
        userAnnotation.setUserId(authUtil.getCurrentUserId());
        userAnnotation.setMusicId(musicId);
        userAnnotation.setNote(requestDTO.note());
        userAnnotation.setExcerpt(requestDTO.excerpt());
        userAnnotation.setStartIndex(requestDTO.startIndex());
        userAnnotation.setEndIndex(requestDTO.endIndex());
        userAnnotation.setTimestamp(LocalDateTime.now());
        annotationRepository.save(userAnnotation);

        return new AnnotationResponseDTO(userAnnotation.getId(), userAnnotation.getUserId(), userAnnotation.getMusicId(), userAnnotation.getNote(), userAnnotation.getExcerpt(), userAnnotation.getStartIndex(), userAnnotation.getEndIndex(), userAnnotation.getTimestamp());
    }
}
