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
    @Column(length = 25)
    private String compositor;
    @Column(length = 25)
    private String album;
    private String genre;
    @Column(length = 10000)
    private String lyrics;
    @Column(nullable = false)
    private int likeCount = 0;
    private int commentCount = 0;
}