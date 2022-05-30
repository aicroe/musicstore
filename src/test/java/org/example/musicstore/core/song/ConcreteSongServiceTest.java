package org.example.musicstore.core.song;

import org.example.musicstore.core.artist.Artist;
import org.example.musicstore.core.artist.ArtistRepository;
import org.example.musicstore.core.common.exception.DuplicatedItemCoreException;
import org.example.musicstore.core.common.exception.NotFoundCoreException;
import org.example.musicstore.core.common.exception.ValidationCoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConcreteSongServiceTest {

    private SongRepository songRepository;
    private ArtistRepository artistRepository;
    private ConcreteSongService service;
    private String artistName;

    @BeforeEach
    public void setUp() {
        artistName = "Marti";
        songRepository = mock(SongRepository.class);
        artistRepository = mock(ArtistRepository.class);
        service = new ConcreteSongService(songRepository, artistRepository, new SongValidator());
    }

    @Test
    public void create_whenSongIsNew_thenReturnsNotNull() {
        var song = new Song("Bored", artistName);
        when(songRepository.create(any())).thenReturn(song);
        when(artistRepository.get(artistName)).thenReturn(Optional.of(new Artist(artistName)));

        var result = service.create(song);

        assertNotNull(result);
    }

    @Test
    public void create_whenTitleIsEmpty_thenThrowsException() {
        var song = new Song("", artistName);

        assertThrows(ValidationCoreException.class, () -> service.create(song));
    }

    @Test
    public void create_whenSongTitleIsAlreadyUsed_thenReturnsNotNull() {
        var song = new Song("Bored", artistName);
        when(songRepository.getAll()).thenReturn(List.of(song));
        when(artistRepository.get(artistName)).thenReturn(Optional.of(new Artist(artistName)));

        assertThrows(DuplicatedItemCoreException.class, () -> service.create(song));
    }

    @Test
    public void create_whenArtistDoesNotExist_thenThrowsException() {
        var song = new Song("Bored", "Mary");

        assertThrows(NotFoundCoreException.class, () -> service.create(song));
    }

    @Test
    public void create_whenArtistNameIsEmpty_thenThrowsException() {
        var song = new Song("Bored", "");

        assertThrows(ValidationCoreException.class, () -> service.create(song));
    }

    @Test
    public void get_whenSongIsPresent_thenReturnsSong() {
        var song = new Song("Bored", artistName);
        when(songRepository.get(any())).thenReturn(Optional.of(song));

        var result = service.get("Bored");

        assertEquals(song, result);
    }

    @Test
    public void get_whenSongIsNotPresent_thenThrowsException() {
        when(songRepository.get(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.get("Bored"));
    }

    @Test
    public void update_whenSongIsPresent_thenReturnsNewSong() {
        var song = new Song(UUID.randomUUID(), "Try", artistName);
        when(songRepository.update(any())).thenReturn(Optional.of(song));

        var result = service.update(song);

        assertEquals(song, result);
    }

    @Test
    public void update_whenSongIsNotPresent_thenThrowsException() {
        var song = new Song(UUID.randomUUID(), "Try", artistName);
        when(songRepository.update(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.update(song));
    }

    @Test
    public void update_whenSongTitleIsEmpty_thenThrowsException() {
        var service = this.service;

        assertThrows(ValidationCoreException.class, () ->
            service.update(new Song(UUID.randomUUID(), "", artistName))
        );
    }

    @Test
    public void update_whenSongTitleIsAlreadyUsed_thenThrowsException() {
        var id = UUID.randomUUID();
        when(songRepository.getAll()).thenReturn(List.of(
            new Song(id, "Bored", artistName),
            new Song(UUID.randomUUID(), "Move", artistName))
        );

        assertThrows(DuplicatedItemCoreException.class, () -> service.update(new Song(id, "Move", artistName)));
    }

    @Test
    public void delete_whenSongIsPresent_thenReturnsSong() {
        var song = new Song("Taken", artistName);
        var service = this.service;
        when(songRepository.delete(any())).thenReturn(Optional.of(song));

        var result = service.delete("Taken");

        assertEquals(song, result);
    }

    @Test
    public void delete_whenSongIsNotPresent_thenReturnsSong() {
        when(songRepository.delete(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.delete("Taken"));
    }
}