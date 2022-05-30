package org.example.musicstore.core.album;

import org.example.musicstore.core.artist.Artist;
import org.example.musicstore.core.artist.ArtistRepository;
import org.example.musicstore.core.song.Song;
import org.example.musicstore.core.song.SongRepository;
import org.example.musicstore.core.common.exception.DuplicatedItemCoreException;
import org.example.musicstore.core.common.exception.KeyConstraintCoreException;
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

class ConcreteAlbumServiceTest {

    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private SongRepository songRepository;
    private ConcreteAlbumService service;
    private String artistName;

    @BeforeEach
    public void setUp() {
        albumRepository = mock(AlbumRepository.class);
        artistRepository = mock(ArtistRepository.class);
        songRepository = mock(SongRepository.class);
        service = new ConcreteAlbumService(albumRepository, artistRepository, songRepository, new AlbumValidator());
        artistName = "Yor";
    }

    @Test
    public void create_whenAlbumIsNew_thenReturnsNotNull() {
        var album = new Album("Bad Ideas", artistName);
        when(albumRepository.create(any())).thenReturn(album);
        when(artistRepository.get(artistName)).thenReturn(Optional.of(new Artist(artistName)));

        var result = service.create(album);

        assertNotNull(result);
    }

    @Test
    public void create_whenAlbumTitleIsEmpty_thenThrowsException() {
        assertThrows(ValidationCoreException.class, () -> service.create(new Album("", artistName)));
    }

    @Test
    public void create_whenAlbumNameIsAlreadyUsed_thenThrowsException() {
        var album = new Album("Bad Ideas", artistName);
        when(albumRepository.getAll()).thenReturn(List.of(album));
        when(artistRepository.get(artistName)).thenReturn(Optional.of(new Artist(artistName)));

        assertThrows(DuplicatedItemCoreException.class, () -> service.create(album));
    }

    @Test
    public void create_whenArtistNameIsEmpty_thenThrowsException() {
        var album = new Album("Bad Ideas", "");

        assertThrows(ValidationCoreException.class, () -> service.create(album));
    }

    @Test
    public void create_whenArtistNameDoesNotExist_thenThrowsException() {
        var album = new Album("Bad Ideas", "Mary");

        assertThrows(NotFoundCoreException.class, () -> service.create(album));
    }

    @Test
    public void get_whenAlbumIsPresent_thenReturnAlbum() {
        var album = new Album("Say My Name", artistName);
        when(albumRepository.get(any())).thenReturn(Optional.of(album));

        var result = service.get("Say My Name");

        assertEquals(album, result);
    }

    @Test
    public void get_whenAlbumIsNotPresent_thenThrowsException() {
        when(albumRepository.get(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.get("Say My Name"));
    }

    @Test
    public void update_whenAlbumIsPresent_thenReturnsNewAlbum() {
        var id = UUID.randomUUID();
        var album = new Album(id, "Bad Ideas", artistName);
        when(albumRepository.update(any())).thenReturn(Optional.of(album));

        var result = service.update(new Album(id, "Bad Ideas", artistName));

        assertEquals(album, result);
    }

    @Test
    public void update_whenAlbumIsNotPresent_thenThrowsException() {
        when(albumRepository.update(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () ->
            service.update(new Album(UUID.randomUUID(), "Bad Ideas", artistName))
        );
    }

    @Test
    public void update_whenAlbumTitleIsEmpty_thenThrowsException() {
        assertThrows(ValidationCoreException.class, () ->
            service.update(new Album(UUID.randomUUID(), "", artistName))
        );
    }

    @Test
    public void update_whenAlbumTitleIsAlreadyUsed_thenThrowsException() {
        var id = UUID.randomUUID();
        when(albumRepository.getAll()).thenReturn(List.of(
            new Album(id, "Bad Ideas", artistName),
            new Album(UUID.randomUUID(), "Say My Name", artistName))
        );

        assertThrows(DuplicatedItemCoreException.class, () ->
            service.update(new Album(id, "Say My Name", artistName))
        );
    }

    @Test
    public void delete_whenAlbumIsPresent_thenReturnsAlbum() {
        var album = new Album(UUID.randomUUID(), "Bad Ideas", artistName);
        when(albumRepository.delete(any())).thenReturn(Optional.of(album));

        var result = service.delete("Bad Ideas");

        assertEquals(album, result);
    }

    @Test
    public void delete_whenAlbumIsNotPresent_thenThrowsException() {
        when(albumRepository.delete(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.delete("Bad Ideas"));
    }

    @Test
    public void delete_whenThereAreSongsRelated_thenThrowsException() {
        when(albumRepository.getSongs("Bad Ideas")).thenReturn(List.of(new Song("Crush", artistName)));

        assertThrows(KeyConstraintCoreException.class, () -> service.delete("Bad Ideas"));
    }

    @Test
    public void addSong_whenBothAlbumAndSongExist_thenWorks() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.of(song));
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.of(album));
        when(albumRepository.addSong(album.getTitle(), song.getTitle())).thenReturn(true);

        assertTrue(service.addSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void addSong_whenAlbumDoesNotExist_thenThrowsException() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.of(song));
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.addSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void addSong_whenSongDoesNotExist_thenThrowsException() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.empty());
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.of(album));

        assertThrows(NotFoundCoreException.class, () -> service.addSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void addSong_whenArtistsDoNotMatch_thenThrowsException() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", "Tessa");
        when(songRepository.get(song.getTitle())).thenReturn(Optional.of(song));
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.of(album));

        assertThrows(KeyConstraintCoreException.class, () -> service.addSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void addSong_whenSongIsAlreadyThere_thenReturnsFalse() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.of(song));
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.of(album));
        when(albumRepository.getSongs("Bad Ideas")).thenReturn(List.of(song));

        assertFalse(service.addSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void deleteSong_whenBothAlbumAndSongExist_thenWorks() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.of(song));
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.of(album));
        when(albumRepository.deleteSong(album.getTitle(), song.getTitle())).thenReturn(true);

        assertTrue(service.deleteSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void deleteSong_whenAlbumDoesNotExist_thenThrowsException() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.of(song));
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.empty());

        assertThrows(NotFoundCoreException.class, () -> service.deleteSong(album.getTitle(), song.getTitle()));
    }

    @Test
    public void deleteSong_whenSongDoesNotExist_thenThrowsException() {
        var song = new Song("Crush", artistName);
        var album = new Album("Bad Ideas", artistName);
        when(songRepository.get(song.getTitle())).thenReturn(Optional.empty());
        when(albumRepository.get(album.getTitle())).thenReturn(Optional.of(album));

        assertThrows(NotFoundCoreException.class, () -> service.deleteSong(album.getTitle(), song.getTitle()));
    }
}