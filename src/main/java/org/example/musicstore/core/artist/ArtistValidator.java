package org.example.musicstore.core.artist;

import org.example.musicstore.core.common.exception.CoreException;
import org.example.musicstore.core.common.exception.ValidationCoreException;
import org.example.musicstore.core.common.validation.ObjectValidator;

import static org.example.musicstore.core.common.validation.Validators.checkEmpty;

public class ArtistValidator implements ObjectValidator<Artist, CoreException> {

    @Override
    public void validate(Artist artist) {
        checkEmpty(artist.getName(), () -> ValidationCoreException.forEmptyField("name"));
    }
}
