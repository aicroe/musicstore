package org.example.musicstore.persistence.artist;

import org.example.musicstore.core.artist.Artist;
import org.example.musicstore.core.artist.ArtistRepository;
import org.example.musicstore.persistence.Converters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ArtistAdapter implements ArtistRepository {

    private ArtistDao artistDao;

    public ArtistAdapter(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public List<Artist> getAll() {
        return artistDao.findAll().stream().map(Converters::toArtist).collect(Collectors.toList());
    }

    @Override
    public Artist create(Artist artist) {
        var artistToSave = new org.example.musicstore.persistence.artist.Artist();
        artistToSave.setId(UUID.randomUUID());
        artistToSave.setName(artist.getName());

        return Converters.toArtist(artistDao.save(artistToSave));
    }

    @Override
    public Optional<Artist> get(String name) {
        return artistDao.findByName(name).map(Converters::toArtist);
    }

    @Override
    public Optional<Artist> update(Artist artist) {
        var wrapper = artistDao.findById(artist.getId());
        if (wrapper.isEmpty()) {
            return Optional.empty();
        }

        var current = wrapper.get();
        current.setName(artist.getName());
        var updated = artistDao.save(current);

        return Optional.of(updated).map(Converters::toArtist);
    }

    @Override
    public Optional<Artist> delete(String name) {
        var current = artistDao.findByName(name);
        current.ifPresent(value -> artistDao.deleteById(value.getId()));

        return current.map(Converters::toArtist);
    }
}
