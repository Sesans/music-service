package com.ms.music_service.dto.music;

import lombok.Getter;

@Getter
public enum SearchSort {
    LIKE_COUNT("likeCount"),
    COMMENT_COUNT("commentCount");

    private final String field;
    SearchSort(String field){
        this.field = field;
    }
}