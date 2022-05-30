package org.example.musicstore.core.album;

import org.example.musicstore.core.song.Song;

import java.util.List;

public interface AlbumService {
    List<Album> getAll();
    List<Album> getByArtistName(String artistName);
    List<Song> getSongs(String albumTitle);
    Album get(String albumTitle);
    Album create(Album album);
    Album update(Album album);
    Album delete(String albumTitle);
    boolean addSong(String albumTitle, String songTitle);
    boolean deleteSong(String albumTitle, String songTitle);
}
