package com.ms.music_service.repository;

import com.ms.music_service.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    boolean existsByName(String name);
}
