package org.example.musicstore.persistence.song;

import org.example.musicstore.core.song.Song;
import org.example.musicstore.core.song.SongRepository;
import org.example.musicstore.persistence.Converters;
import org.example.musicstore.persistence.artist.ArtistDao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SongAdapter implements SongRepository {

    private SongDao songDao;
    private ArtistDao artistDao;

    public SongAdapter(SongDao songDao, ArtistDao artistDao) {
        this.songDao = songDao;
        this.artistDao = artistDao;
    }

    @Override
    public List<Song> getAll() {
        return songDao.findAll().stream().map(Converters::toSong).collect(Collectors.toList());
    }

    @Override
    public List<Song> getByArtistName(String artistName) {
        return songDao.findByArtistName(artistName).stream().map(Converters::toSong).collect(Collectors.toList());
    }

    @Override
    public Song create(Song song) {
        var artist = artistDao.findByName(song.getArtistName());
        var songToSave = new org.example.musicstore.persistence.song.Song();
        songToSave.setId(UUID.randomUUID());
        songToSave.setTitle(song.getTitle());
        artist.ifPresent(songToSave::setArtist);

        return Converters.toSong(songDao.save(songToSave));
    }

    @Override
    public Optional<Song> get(String title) {
        return songDao.findByTitle(title).map(Converters::toSong);
    }

    @Override
    public Optional<Song> update(Song song) {
        var wrapper = songDao.findById(song.getId());
        if (wrapper.isEmpty()) {
            return Optional.empty();
        }

        var current = wrapper.get();
        current.setTitle(song.getTitle());
        var updated = songDao.save(current);

        return Optional.of(updated).map(Converters::toSong);
    }

    @Override
    public Optional<Song> delete(String title) {
        var current = songDao.findByTitle(title);
        current.ifPresent(value -> songDao.deleteById(value.getId()));

        return current.map(Converters::toSong);
    }
}
