package org.example.musicstore.persistence.album;

import org.example.musicstore.core.album.Album;
import org.example.musicstore.core.album.AlbumRepository;
import org.example.musicstore.core.song.Song;
import org.example.musicstore.persistence.Converters;
import org.example.musicstore.persistence.artist.ArtistDao;
import org.example.musicstore.persistence.song.SongDao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AlbumAdapter implements AlbumRepository {

    private AlbumDao albumDao;
    private ArtistDao artistDao;
    private SongDao songDao;

    public AlbumAdapter(AlbumDao albumDao, ArtistDao artistDao, SongDao songDao) {
        this.albumDao = albumDao;
        this.artistDao = artistDao;
        this.songDao = songDao;
    }

    @Override
    public List<Album> getAll() {
        return this.albumDao.findAll().stream().map(Converters::toAlbum).collect(Collectors.toList());
    }

    @Override
    public List<Album> getByArtistName(String artistName) {
        return albumDao.findByArtistName(artistName).stream().map(Converters::toAlbum).collect(Collectors.toList());
    }

    @Override
    public List<Song> getSongs(String albumTitle) {
        return albumDao.findByTitle(albumTitle)
            .map(org.example.musicstore.persistence.album.Album::getSongs)
            .map(songs -> songs.stream().map(Converters::toSong).collect(Collectors.toList()))
            .orElse(List.of());
    }

    @Override
    public Album create(Album album) {
        var artist = artistDao.findByName(album.getArtistName());
        var albumToSave = new org.example.musicstore.persistence.album.Album();
        albumToSave.setId(UUID.randomUUID());
        albumToSave.setTitle(album.getTitle());
        artist.ifPresent(albumToSave::setArtist);

        return Converters.toAlbum(albumDao.save(albumToSave));
    }

    @Override
    public Optional<Album> get(String albumTitle) {
        return albumDao.findByTitle(albumTitle).map(Converters::toAlbum);
    }

    @Override
    public Optional<Album> update(Album album) {
        var wrapper = albumDao.findById(album.getId());
        if (wrapper.isEmpty()) {
            return Optional.empty();
        }

        var current = wrapper.get();
        current.setTitle(album.getTitle());
        var updated = albumDao.save(current);

        return Optional.of(updated).map(Converters::toAlbum);
    }

    @Override
    public Optional<Album> delete(String albumTitle) {
        var current = albumDao.findByTitle(albumTitle);
        current.ifPresent(value -> albumDao.deleteById(value.getId()));

        return current.map(Converters::toAlbum);
    }

    @Override
    public boolean addSong(String albumTitle, String songTitle) {
        var savedAlbum = albumDao.findByTitle(albumTitle);
        var savedSong = songDao.findByTitle(songTitle);
        if (savedAlbum.isEmpty() || savedSong.isEmpty()) {
            return false;
        }

        var currentAlbum = savedAlbum.get();
        currentAlbum.getSongs().add(savedSong.get());
        albumDao.save(currentAlbum);

        return true;
    }

    @Override
    public boolean deleteSong(String albumTitle, String songTitle) {
        var savedAlbum = albumDao.findByTitle(albumTitle);
        var savedSong = songDao.findByTitle(songTitle);
        if (savedAlbum.isEmpty() || savedSong.isEmpty()) {
            return false;
        }

        var currentAlbum = savedAlbum.get();
        var songs = currentAlbum.getSongs()
            .stream()
            .filter(song -> !song.getTitle().equals(songTitle))
            .collect(Collectors.toList());
        currentAlbum.setSongs(songs);
        albumDao.save(currentAlbum);

        return true;
    }
}
