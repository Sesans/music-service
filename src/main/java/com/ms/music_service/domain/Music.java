package com.ms.music_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_music")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;
    @Column(length = 50)
    private String album;
    private String genre;
    @Column(length = 10000)
    private String lyrics;
    @Transient
    private boolean liked;
    @Column(nullable = false)
    private int likeCount = 0;
    @Column(nullable = false)
    private int commentCount = 0;
}