package org.example.musicstore.core.song;

import java.util.List;

public interface SongService {
    List<Song> getAll();
    List<Song> getByArtistName(String artist);
    Song get(String title);
    Song create(Song song);
    Song update(Song song);
    Song delete(String title);
}
