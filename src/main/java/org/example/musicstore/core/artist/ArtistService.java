package org.example.musicstore.core.artist;

import java.util.List;

public interface ArtistService {
    List<Artist> getAll();
    Artist get(String name);
    Artist create(Artist artist);
    Artist update(Artist artist);
    Artist delete(String name);
}
