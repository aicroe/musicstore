package org.example.musicstore.core.artist;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {
    List<Artist> getAll();
    Artist create(Artist artist);
    Optional<Artist> get(String name);
    Optional<Artist> update(Artist artist);
    Optional<Artist> delete(String name);
}
