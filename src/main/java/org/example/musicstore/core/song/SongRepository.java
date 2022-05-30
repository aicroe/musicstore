package org.example.musicstore.core.song;

import java.util.List;
import java.util.Optional;

public interface SongRepository {
    List<Song> getAll();
    List<Song> getByArtistName(String artistName);
    Song create(Song song);
    Optional<Song> get(String title);
    Optional<Song> update(Song song);
    Optional<Song> delete(String title);
}
