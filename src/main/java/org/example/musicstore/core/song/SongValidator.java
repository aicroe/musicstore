package org.example.musicstore.core.song;

import org.example.musicstore.core.common.exception.CoreException;
import org.example.musicstore.core.common.exception.ValidationCoreException;
import org.example.musicstore.core.common.validation.ObjectValidator;
import org.example.musicstore.core.common.validation.Validators;

public class SongValidator implements ObjectValidator<Song, CoreException> {

    @Override
    public void validate(Song song) {
        Validators.checkEmpty(song.getTitle(), () -> ValidationCoreException.forEmptyField("title"));
    }
}
