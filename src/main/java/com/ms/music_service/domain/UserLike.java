package com.ms.music_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_like")
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDateTime timestamp;
    @ManyToOne
    @Column(name = "music_id")
    private Integer musicId;
    @Embedded
    @ManyToOne
    @Column(name = "user_id")
    private UUID userId;
}
