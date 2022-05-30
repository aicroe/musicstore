package org.example.musicstore.persistence.song;

import org.example.musicstore.persistence.artist.Artist;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Song {

    @Id
    private UUID id;

    private String title;

    @ManyToOne(optional = false)
    private Artist artist;

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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
