package org.example.musicstore.persistence.album;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumDao extends JpaRepository<Album, UUID> {
    List<Album> findByArtistName(String name);
    Optional<Album> findByTitle(String albumTitle);
}
