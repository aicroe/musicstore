package org.example.musicstore.core.artist;

import org.example.musicstore.core.album.Album;
import org.example.musicstore.core.album.AlbumRepository;
import org.example.musicstore.core.common.exception.DuplicatedItemCoreException;
import org.example.musicstore.core.common.exception.KeyConstraintCoreException;
import org.example.musicstore.core.common.exception.NotFoundCoreException;
import org.example.musicstore.core.common.exception.ValidationCoreException;
import org.example.musicstore.core.song.Song;
import org.example.musicstore.core.song.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConcreteArtistServiceTest {

    private ArtistRepository artistRepository;
    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ConcreteArtistService service;

    @BeforeEach
    public void setUp() {
        artistRepository = mock(ArtistRepository.class);
        songRepository = mock(SongRepository.class);
        albumRepository = mock(AlbumRepository.class);
        service = new ConcreteArtistService(artistRepository, songRepository, albumRepository, new ArtistValidator());
    }

    @Test
    public void create_whenArtistIsNew_thenReturnsNotNull() {
        var artist = new Artist("John");
        when(artistRepository.create(any())).thenReturn(artist);

        var result = service.create(artist);

        assertNotNull(result);
    }

    @Test
    public void create_whenArtistNameIsEmpty_thenThrowsException() {
        assertThrows(ValidationCoreException.class, () -> service.create(new Artist("")));
    }

    @Test
    public void create_whenArtistNameIsAlreadyUsed_thenThrowsException() {
        var id = UUID.randomUUID();
        var artist = new Artist(id, "Taco");
        when(artistRepository.getAll()).thenReturn(List.of(artist));

        assertThrows(DuplicatedItemCoreException.class, () -> service.create(artist));
    }

    @Test
    public void get_whenArtistIsPresent_thenReturnsArtist() {
        var artist = new Artist("Gojou");
        when(artistRepository.get(any())).thenReturn(Optional.of(artist));

        var result = service.get("Gojou");

        assertEquals(artist, result);
    }

    @Test
    public void get_whenArtistIsNotPresent_thenThrowsException() {
        when(artistRepository.get(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.get("Gojou"));
    }

    @Test
    public void update_whenArtistIsPresent_thenReturnsNewArtist() {
        var artist = new Artist(UUID.randomUUID(), "Gojou");
        when(artistRepository.update(any())).thenReturn(Optional.of(artist));

        var result = service.update(artist);

        assertEquals(artist, result);
    }

    @Test
    public void update_whenNewNameIsEmpty_thenThrowsException() {
        assertThrows(ValidationCoreException.class, () -> service.update(new Artist(UUID.randomUUID(), "")));
    }

    @Test
    public void update_whenArtistNameIsAlreadyUsed_thenThrowsException() {
        var id = UUID.randomUUID();
        when(artistRepository.getAll()).thenReturn(List.of(new Artist(id, "TJ"), new Artist(UUID.randomUUID(), "DJ")));

        assertThrows(DuplicatedItemCoreException.class, () -> service.update(new Artist(id, "DJ")));
    }

    @Test
    public void update_whenArtistIsNotPresent_thenThrowsException() {
        assertThrows(NotFoundCoreException.class, () -> service.update(new Artist(UUID.randomUUID(), "DJ")));
    }

    @Test
    public void delete_whenArtistIsPresent_thenReturnsArtist() {
        var artist = new Artist(UUID.randomUUID(), "Dante");
        when(artistRepository.delete(any())).thenReturn(Optional.of(artist));

        var result = service.delete("Dante");

        assertEquals(artist, result);
    }

    @Test
    public void delete_whenIsNotPresent_thenThrowsException() {
        when(artistRepository.delete(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.delete("Dante"));
    }

    @Test
    public void delete_whenThereAreSongsRelated_thenThrowsException() {
        when(songRepository.getByArtistName(any())).thenReturn(List.of(new Song("Thrift Shop", "Mar")));

        assertThrows(KeyConstraintCoreException.class, () -> service.delete("Mar"));
    }

    @Test
    public void delete_whenThereAreAlbumsRelated_thenThrowsException() {
        when(albumRepository.getByArtistName(any())).thenReturn(List.of(new Album("Bad Ideass", "Mar")));

        assertThrows(KeyConstraintCoreException.class, () -> service.delete("Mar"));
    }
}