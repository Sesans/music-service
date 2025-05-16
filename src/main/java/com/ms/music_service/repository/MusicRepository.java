package com.ms.music_service.repository;

import com.ms.music_service.domain.Music;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MusicRepository extends JpaRepository<Music, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Music m SET m.likeCount = m.likeCount + 1 WHERE m.id = :musicId")
    void incrementLikeCount(@Param("musicId") Long musicId);
}