package org.example.musicstore.core.album;

import org.example.musicstore.core.common.exception.CoreException;
import org.example.musicstore.core.common.exception.ValidationCoreException;
import org.example.musicstore.core.common.validation.ObjectValidator;

import static org.example.musicstore.core.common.validation.Validators.checkEmpty;

public class AlbumValidator implements ObjectValidator<Album, CoreException> {
    @Override
    public void validate(Album album) {
        checkEmpty(album.getTitle(), () -> ValidationCoreException.forEmptyField("title"));
    }
}
