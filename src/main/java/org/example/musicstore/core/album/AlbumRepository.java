package org.example.musicstore.core.album;

import org.example.musicstore.core.song.Song;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository {
    List<Album> getAll();
    List<Album> getByArtistName(String artistName);
    List<Song> getSongs(String albumTitle);
    Album create(Album album);
    Optional<Album> get(String albumTitle);
    Optional<Album> update(Album album);
    Optional<Album> delete(String albumTitle);
    boolean addSong(String albumTitle, String songTitle);
    boolean deleteSong(String albumTitle, String songTitle);
}
