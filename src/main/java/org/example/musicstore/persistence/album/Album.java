package org.example.musicstore.persistence.album;

import org.example.musicstore.persistence.artist.Artist;
import org.example.musicstore.persistence.song.Song;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Album {

    @Id
    private UUID id;

    private String title;

    @ManyToOne(optional = false)
    private Artist artist;

    @ManyToMany
    private List<Song> songs;

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

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
