package com.ms.music_service.repository;

import com.ms.music_service.domain.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<UserComment, Long> {
    List<UserComment> findByMusicId(Long musicId);
}
