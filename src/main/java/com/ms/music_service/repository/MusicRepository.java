package com.ms.music_service.repository;

import com.ms.music_service.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Integer> {
}