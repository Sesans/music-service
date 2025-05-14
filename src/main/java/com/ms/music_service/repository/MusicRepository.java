package com.ms.music_service.repository;

import com.ms.music_service.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MusicRepository extends JpaRepository<Music, Integer> {
    @Query("UPDATE tb_music m SET m.like_count = m.like_count + 1 where m.id = :musicId")
    void incrementLikeCount(Integer musicId);
}