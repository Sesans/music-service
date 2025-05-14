package com.ms.music_service.repository;

import com.ms.music_service.domain.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepositoy extends JpaRepository<UserComment, Long> {
}
