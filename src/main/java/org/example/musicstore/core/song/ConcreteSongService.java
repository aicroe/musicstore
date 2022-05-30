package org.example.musicstore.core.song;

import org.example.musicstore.core.artist.ArtistRepository;
import org.example.musicstore.core.common.validation.ObjectValidator;
import org.example.musicstore.core.common.exception.CoreException;
import org.example.musicstore.core.common.exception.DuplicatedItemCoreException;
import org.example.musicstore.core.common.exception.NotFoundCoreException;
import org.example.musicstore.core.common.exception.ValidationCoreException;
import org.example.musicstore.core.common.validation.Validators;

import java.util.List;
import java.util.stream.Collectors;

public class ConcreteSongService implements SongService {

    private SongRepository songRepository;
    private ArtistRepository artistRepository;
    private ObjectValidator<Song, CoreException> validator;

    public ConcreteSongService(
            SongRepository songRepository,
            ArtistRepository artistRepository,
            ObjectValidator<Song, CoreException> validator) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.validator = validator;
    }

    @Override
    public List<Song> getAll() {
        return songRepository.getAll();
    }

    @Override
    public List<Song> getByArtistName(String artistName) {
        return songRepository.getByArtistName(artistName);
    }

    @Override
    public Song get(String title) {
        return songRepository.get(title).orElseThrow(() -> NotFoundCoreException.forKey(title));
    }

    @Override
    public Song create(Song song) {
        validator.validate(song);

        var artistName = song.getArtistName();
        Validators.checkEmpty(artistName, () -> ValidationCoreException.forEmptyField("artistName"));
        Validators.checkCondition(artistRepository.get(artistName).isEmpty(), () -> NotFoundCoreException.forKey(artistName));

        var songs = songRepository.getAll()
            .stream()
            .map(Song::getTitle)
            .collect(Collectors.toList());
        Validators.checkDuplicate(songs, song.getTitle(), () -> DuplicatedItemCoreException.forKey(song.getTitle()));

        return songRepository.create(song);
    }

    @Override
    public Song update(Song song) {
        validator.validate(song);

        var songs = songRepository.getAll()
            .stream()
            .filter(each -> !each.getId().equals(song.getId()))
            .map(Song::getTitle)
            .collect(Collectors.toList());
        Validators.checkDuplicate(songs, song.getTitle(), () -> DuplicatedItemCoreException.forKey(song.getTitle()));

        return songRepository.update(song).orElseThrow(() -> NotFoundCoreException.forKey(song.getTitle()));
    }

    @Override
    public Song delete(String title) {
        return songRepository.delete(title).orElseThrow(() -> NotFoundCoreException.forKey(title));
    }
}
