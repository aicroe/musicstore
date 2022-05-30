package org.example.musicstore.api;

import org.example.musicstore.core.album.Album;
import org.example.musicstore.core.album.AlbumService;
import org.example.musicstore.core.song.Song;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/album")
@RestController
public class AlbumController {

    private AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/all")
    public List<Album> getAll() {
        return albumService.getAll();
    }

    @GetMapping("/artist/{name}")
    public List<Album> getByArtistName(@PathVariable String name) {
        return albumService.getByArtistName(name);
    }

    @GetMapping("/{title}")
    public Album getAlbum(@PathVariable String title) {
        return albumService.get(title);
    }

    @PostMapping
    public Album createAlbum(@RequestBody Album album) {
        return albumService.create(album);
    }

    @PutMapping
    public Album updateAlbum(@RequestBody Album album) {
        return albumService.update(album);
    }

    @DeleteMapping("/{title}")
    public Album deleteAlbum(@PathVariable String title) {
        return albumService.delete(title);
    }

    @GetMapping("/song/{albumTitle}")
    public List<Song> getSongsByAlbumTitle(@PathVariable String albumTitle) {
        return albumService.getSongs(albumTitle);
    }

    @PostMapping("/song")
    public ResponseEntity<Object> addSong(@RequestBody Map<String, String> params) {
        albumService.addSong(params.get("albumTitle"), params.get("songTitle"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/song")
    public ResponseEntity<Object> deleteSong(@RequestBody Map<String, String> params) {
        albumService.deleteSong(params.get("albumTitle"), params.get("songTitle"));
        return ResponseEntity.ok().build();
    }
}
