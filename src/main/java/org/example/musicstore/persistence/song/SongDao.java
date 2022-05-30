package org.example.musicstore.persistence.song;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SongDao extends JpaRepository<Song, UUID> {
    List<Song> findByArtistName(String artistName);
    Optional<Song> findByTitle(String title);
}
