package com.ms.music_service.dto.music;

import java.util.List;

public record PagedResponseDTO(
        List<MusicPageDTO> songs,
        boolean hasNext
) {
}