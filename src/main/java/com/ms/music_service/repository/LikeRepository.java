package com.ms.music_service.repository;

import com.ms.music_service.domain.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
    boolean existsByUserIdAndMusicId(UUID userId, Long musicId);
}
