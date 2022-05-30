package org.example.musicstore.core.song;

import java.util.UUID;

public class Song {

    private UUID id;
    private String title;
    private String artistName;

    public Song() { }

    public Song(UUID id, String title, String artistName) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
    }

    public Song(String title, String artistName) {
        this.title = title;
        this.artistName = artistName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
