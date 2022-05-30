package org.example.musicstore.persistence.artist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArtistDao extends JpaRepository<Artist, UUID> {
    Optional<Artist> findByName(String name);
}
