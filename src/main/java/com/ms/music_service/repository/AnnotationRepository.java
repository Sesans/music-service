package com.ms.music_service.repository;

import com.ms.music_service.domain.UserAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnotationRepository extends JpaRepository<UserAnnotation, Long> {
}
