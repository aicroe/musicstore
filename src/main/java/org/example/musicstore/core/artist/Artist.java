package org.example.musicstore.core.artist;

import java.util.UUID;

public class Artist {
    private UUID id;
    private String name;

    public Artist() { }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
