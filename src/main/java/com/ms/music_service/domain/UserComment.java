package com.ms.music_service.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_comment")
public class UserComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID userId;
    @ManyToOne
    @JoinColumn(name = "music_id")
    private Music music;
    private String comment;
}