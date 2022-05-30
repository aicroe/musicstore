package org.example.musicstore.core.artist;

import org.example.musicstore.core.album.AlbumRepository;
import org.example.musicstore.core.common.exception.CoreException;
import org.example.musicstore.core.common.exception.DuplicatedItemCoreException;
import org.example.musicstore.core.common.exception.KeyConstraintCoreException;
import org.example.musicstore.core.common.exception.NotFoundCoreException;
import org.example.musicstore.core.song.SongRepository;
import org.example.musicstore.core.common.validation.ObjectValidator;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.musicstore.core.common.validation.Validators.*;

public class ConcreteArtistService implements ArtistService {

    private ArtistRepository artistRepository;
    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ObjectValidator<Artist, CoreException> validator;
    
    public ConcreteArtistService(
            ArtistRepository artistRepository,
            SongRepository songRepository,
            AlbumRepository albumRepository,
            ObjectValidator<Artist, CoreException> validator) {
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.validator = validator;
    }

    @Override
    public Artist create(Artist artist) {
        validator.validate(artist);

        var artistNames = artistRepository.getAll()
            .stream()
            .map(Artist::getName)
            .collect(Collectors.toList());
        checkDuplicate(artistNames, artist.getName(), () -> DuplicatedItemCoreException.forKey(artist.getName()));

        return this.artistRepository.create(artist);
    }

    @Override
    public List<Artist> getAll() {
        return this.artistRepository.getAll();
    }

    @Override
    public Artist get(String name) {
        return artistRepository.get(name).orElseThrow(() -> NotFoundCoreException.forKey(name));
    }

    @Override
    public Artist update(Artist artist) {
        validator.validate(artist);

        var artistNames = artistRepository.getAll()
            .stream()
            .filter(each -> !each.getId().equals(artist.getId()))
            .map(Artist::getName)
            .collect(Collectors.toList());
        checkDuplicate(artistNames, artist.getName(), () -> DuplicatedItemCoreException.forKey(artist.getName()));

        return artistRepository.update(artist).orElseThrow(() -> NotFoundCoreException.forKey(artist.getName()));
    }

    @Override
    public Artist delete(String name) {
        var songs = songRepository.getByArtistName(name);
        var albums = albumRepository.getByArtistName(name);
        checkCondition(!songs.isEmpty(), () -> KeyConstraintCoreException.whenDeleting(name));
        checkCondition(!albums.isEmpty(), () -> KeyConstraintCoreException.whenDeleting(name));

        return artistRepository.delete(name).orElseThrow(() -> NotFoundCoreException.forKey(name));
    }
}
