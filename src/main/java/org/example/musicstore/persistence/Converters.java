package org.example.musicstore.persistence;

import org.example.musicstore.core.album.Album;
import org.example.musicstore.core.artist.Artist;
import org.example.musicstore.core.song.Song;

public class Converters {
    public static Artist toArtist(org.example.musicstore.persistence.artist.Artist artist) {
        return new Artist(artist.getId(), artist.getName());
    }

    public static Song toSong(org.example.musicstore.persistence.song.Song song) {
        return new Song(song.getId(), song.getTitle(), song.getArtist().getName());
    }

    public static Album toAlbum(org.example.musicstore.persistence.album.Album album) {
        return new Album(album.getId(), album.getTitle(), album.getArtist().getName());
    }
}
