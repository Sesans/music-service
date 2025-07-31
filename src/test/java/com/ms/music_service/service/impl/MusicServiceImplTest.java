package com.ms.music_service.service.impl;

import com.ms.music_service.domain.Artist;
import com.ms.music_service.domain.Music;
import com.ms.music_service.dto.music.*;
import com.ms.music_service.exception.ConflictException;
import com.ms.music_service.exception.ResourceNotFoundException;
import com.ms.music_service.repository.ArtistRepository;
import com.ms.music_service.repository.LikeRepository;
import com.ms.music_service.repository.MusicRepository;
import com.ms.music_service.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicServiceImplTest {
    @Mock
    MusicRepository musicRepository;
    @Mock
    ArtistRepository artistRepository;
    @Mock
    LikeRepository likeRepository;
    @Mock
    AuthUtil authUtil;

    @InjectMocks
    MusicServiceImpl musicService;

    private MusicRequestDTO requestDTO;
    private Artist artist;
    private Music music;
    private Music music2;
    private UUID userId;

    @BeforeEach
    void setUp(){
        artist = new Artist();
        artist.setId(1L);
        artist.setName("Queen");

        requestDTO = new MusicRequestDTO("Bohemian Rhapsody", artist.getId(), "A Night at the Opera", "Rock", "Is this the real life?");

        music = new Music();
        music.setId(1L);
        music.setTitle(requestDTO.title());
        music.setArtist(artist);
        music.setAlbum(requestDTO.album());
        music.setGenre(requestDTO.genre());
        music.setLyrics(requestDTO.lyrics());
        music.setLiked(true);
        music.setLikeCount(999);
        music.setCommentCount(50);

        music2 = new Music();
        music2.setId(2L);
        music2.setTitle("Song B");
        music2.setArtist(artist);
        music2.setAlbum("Album B");
        music2.setGenre("Genre B");
        music2.setLyrics("Lyrics B");
        music2.setLikeCount(5);
        music2.setCommentCount(1);

        userId = UUID.randomUUID();
    }

    @Test
    void findAll_ShouldReturnListOfMusicResponseDTO() {
        List<Music> musicList = List.of(music);

        when(musicRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount")))
                .thenReturn(musicList);

        List<MusicResponseDTO> responseDTO = musicService.findAll();

        assertEquals(1, responseDTO.size());
        assertEquals("Bohemian Rhapsody", responseDTO.get(0).title());
        assertEquals("Queen", responseDTO.get(0).artist());
        assertEquals("A Night at the Opera", responseDTO.get(0).album());
        assertEquals("Rock", responseDTO.get(0).genre());
        assertEquals("Is this the real life?", responseDTO.get(0).lyrics());
        assertTrue(responseDTO.get(0).liked());
        assertEquals(999, responseDTO.get(0).likeCount());
        assertEquals(50, responseDTO.get(0).commentCount());
    }

    @Test
    void findAll_ShouldThrowRuntimeException(){
        when(musicRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount")))
        .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            musicService.findAll()
        );
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void saveMusic_ShouldSaveSuccessfully(){
        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(musicRepository.existsByTitleAndArtistId(requestDTO.title(), requestDTO.artistId())).thenReturn(false);
        when(musicRepository.save(any(Music.class))).thenReturn(music);

        MusicResponseDTO responseDTO = musicService.saveMusic(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(music.getTitle(), responseDTO.title());
        assertEquals(music.getArtist().getName(), responseDTO.artist());
        assertEquals(music.getAlbum(), responseDTO.album());
        assertEquals(music.getGenre(), responseDTO.genre());
        assertEquals(music.getLyrics(), responseDTO.lyrics());
        assertEquals(0, responseDTO.likeCount());
        assertEquals(0, responseDTO.commentCount());
        assertFalse(responseDTO.liked());

        verify(artistRepository, times(1)).findById(artist.getId());
        verify(musicRepository, times(1)).existsByTitleAndArtistId(requestDTO.title(), requestDTO.artistId());
        verify(musicRepository, times(1)).save(any(Music.class));
    }

    @Test
    void saveMusic_ShouldThrowConflictException(){
        String message = "This song is already registered";
        when(artistRepository.findById(requestDTO.artistId())).thenReturn(Optional.of(artist));
        when(musicRepository.existsByTitleAndArtistId(requestDTO.title(), requestDTO.artistId()))
                .thenThrow(new ConflictException(message));

        ConflictException exception = assertThrows(ConflictException.class, () -> musicService.saveMusic(requestDTO));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void pageList_ShouldReturnPageListOfMusicPageDTO(){
        int page = 0;
        int size = 2;
        String direction = "DESC";
        SearchSort sortBy = SearchSort.LIKE_COUNT;

        List<Music> musicList = List.of(music, music2);
        Page<Music> pageResult = new PageImpl<>(musicList, PageRequest.of(page, size), 5);

        when(musicRepository.findAll(any(Pageable.class))).thenReturn(pageResult);

        PagedResponseDTO response = musicService.pageList(page, size, direction, sortBy);

        assertNotNull(response);
        assertEquals(2, response.songs().size());
        assertTrue(response.hasNext());

        assertEquals(response.songs().get(0).id(), music.getId());
        assertEquals(response.songs().get(1).id(), music2.getId());
        assertEquals(response.songs().get(0).title(), music.getTitle());
        assertEquals(response.songs().get(1).title(), music2.getTitle());
        assertEquals(response.songs().get(0).artist(), music.getArtist().getName());
        assertEquals(response.songs().get(1).artist(), music2.getArtist().getName());
        assertEquals(response.songs().get(0).likeCount(), music.getLikeCount());
        assertEquals(response.songs().get(1).likeCount(), music2.getLikeCount());
        assertEquals(response.songs().get(0).commentCount(), music.getCommentCount());
        assertEquals(response.songs().get(1).commentCount(), music2.getCommentCount());

        verify(musicRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void autoComplete_ShouldReturnListOfMusicSuggestionDTO(){
        String query = "Song B";

        when(musicRepository.findByTitleOrArtist(query)).thenReturn(List.of(music2));

        List<MusicSuggestionDTO> response = musicService.autoComplete(query);

        assertEquals(music2.getId(), response.get(0).id());
        assertEquals(music2.getTitle(), response.get(0).title());
        assertEquals(music2.getArtist().getName(), response.get(0).artist());

        verify(musicRepository, times(1)).findByTitleOrArtist(query);
    }

    @Test
    void autoComplete_ShouldReturnEmptyList(){
        String query = "Song A";

        when(musicRepository.findByTitleOrArtist(query)).thenReturn(Collections.emptyList());

        List<MusicSuggestionDTO> response = musicService.autoComplete(query);

        assertTrue(response.isEmpty());
    }

    @Test
    void getSong_ShouldReturnSongByIdAndSetLikes(){
        when(musicRepository.findById(music2.getId())).thenReturn(Optional.ofNullable(music2));
        when(authUtil.isAuthenticated()).thenReturn(true);
        when(authUtil.getCurrentUserId()).thenReturn(userId);
        when(likeRepository.existsByUserIdAndMusicId(userId, music2.getId())).thenReturn(true);

        MusicResponseDTO response = musicService.getSong(music2.getId());

        assertNotNull(response);
        assertEquals(music2.getId(), response.id());
        assertEquals(music2.getTitle(), response.title());
        assertEquals(music2.getArtist().getName(), response.artist());
        assertEquals(music2.getAlbum(), response.album());
        assertEquals(music2.getGenre(), response.genre());
        assertEquals(music2.getLyrics(), response.lyrics());
        assertTrue(response.liked());
        assertEquals(music2.getLikeCount(), response.likeCount());
        assertEquals(music2.getCommentCount(), response.commentCount());

        verify(likeRepository, times(1)).existsByUserIdAndMusicId(userId, music2.getId());
    }

    @Test
    void getSong_ShouldThrowNotFoundException(){
        when(musicRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> musicService.getSong(99L));

        assertEquals("Song not found with ID: 99", exception.getMessage());
    }
}