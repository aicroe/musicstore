package org.example.musicstore.core.album;

import org.example.musicstore.core.artist.ArtistRepository;
import org.example.musicstore.core.song.Song;
import org.example.musicstore.core.song.SongRepository;
import org.example.musicstore.core.common.validation.ObjectValidator;
import org.example.musicstore.core.common.exception.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.musicstore.core.common.validation.Validators.*;

public class ConcreteAlbumService implements AlbumService {

    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private SongRepository songRepository;
    private ObjectValidator<Album, CoreException> validator;

    public ConcreteAlbumService(
            AlbumRepository albumRepository,
            ArtistRepository artistRepository,
            SongRepository songRepository,
            ObjectValidator<Album, CoreException> validator) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.validator = validator;
    }

    @Override
    public List<Album> getAll() {
        return albumRepository.getAll();
    }

    @Override
    public List<Album> getByArtistName(String artistName) {
        return albumRepository.getByArtistName(artistName);
    }

    @Override
    public List<Song> getSongs(String albumTitle) {
        return albumRepository.getSongs(albumTitle);
    }

    @Override
    public Album get(String albumTitle) {
        return albumRepository.get(albumTitle).orElseThrow(() -> NotFoundCoreException.forKey(albumTitle));
    }

    @Override
    public Album create(Album album) {
        this.validator.validate(album);

        var artistName = album.getArtistName();
        checkEmpty(artistName, () -> ValidationCoreException.forEmptyField("artistName"));
        checkCondition(artistRepository.get(artistName).isEmpty(), () -> NotFoundCoreException.forKey(artistName));

        var albumNames = albumRepository.getAll()
            .stream()
            .map(Album::getTitle)
            .collect(Collectors.toList());
        checkDuplicate(albumNames, album.getTitle(), () -> DuplicatedItemCoreException.forKey(album.getTitle()));

        return albumRepository.create(album);
    }

    @Override
    public Album update(Album album) {
        this.validator.validate(album);

        var albumNames = albumRepository.getAll()
            .stream()
            .filter(each -> !each.getId().equals(album.getId()))
            .map(Album::getTitle)
            .collect(Collectors.toList());
        checkDuplicate(albumNames, album.getTitle(), () -> DuplicatedItemCoreException.forKey(album.getTitle()));

        return albumRepository.update(album).orElseThrow(() -> NotFoundCoreException.forKey(album.getTitle()));
    }

    @Override
    public Album delete(String albumTitle) {
        var songs = albumRepository.getSongs(albumTitle);
        checkCondition(!songs.isEmpty(), () -> KeyConstraintCoreException.whenDeleting(albumTitle));

        return albumRepository.delete(albumTitle).orElseThrow(() -> NotFoundCoreException.forKey(albumTitle));
    }

    @Override
    public boolean addSong(String albumTitle, String songTitle) {
        var album = get(albumTitle);
        var song = songRepository.get(songTitle).orElseThrow(() -> NotFoundCoreException.forKey(songTitle));

        checkCondition(!song.getArtistName().equals(album.getArtistName()), () ->
            KeyConstraintCoreException.whenAddingToCollection(song.getArtistName())
        );

        var albumSongs = getSongs(albumTitle).stream().map(Song::getTitle).collect(Collectors.toList());
        if (!albumSongs.contains(songTitle)) {
            return albumRepository.addSong(albumTitle, songTitle);
        }

        return false;
    }

    @Override
    public boolean deleteSong(String albumTitle, String songTitle) {
        var album = get(albumTitle);
        var song = songRepository.get(songTitle).orElseThrow(() -> NotFoundCoreException.forKey(songTitle));

        assert album != null;
        assert song != null;

        return albumRepository.deleteSong(albumTitle, songTitle);
    }
}
