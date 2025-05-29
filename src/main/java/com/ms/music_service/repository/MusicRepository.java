package com.ms.music_service.repository;

import com.ms.music_service.domain.Music;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Music m SET m.likeCount = m.likeCount + 1 WHERE m.id = :musicId")
    void incrementLikeCount(@Param("musicId") Long musicId);

    @Modifying
    @Transactional
    @Query("UPDATE Music m SET m.likeCount = m.likeCount - 1 WHERE m.id = :musicId")
    void decrementLikeCount(@Param("musicId") Long musicId);

    @Modifying
    @Transactional
    @Query("UPDATE Music m SET m.commentCount = m.commentCount + 1 WHERE m.id = :musicId")
    void incrementCommentCount(@Param("musicId") Long musicId);

    @Query("SELECT m FROM Music m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))" +
            " OR LOWER(m.artist) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Music> findByTitleOrArtist(@Param("query") String query);
}