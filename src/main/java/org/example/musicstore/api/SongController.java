package org.example.musicstore.api;

import org.example.musicstore.core.song.Song;
import org.example.musicstore.core.song.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/song")
@RestController
public class SongController {

    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/all")
    public List<Song> getAll() {
        return songService.getAll();
    }

    @GetMapping("/artist/{name}")
    public List<Song> getByArtistName(@PathVariable String name) {
        return songService.getByArtistName(name);
    }

    @GetMapping("/{title}")
    public Song getSong(@PathVariable String title) {
        return songService.get(title);
    }

    @PostMapping("/")
    public Song createSong(@RequestBody Song song) {
        return songService.create(song);
    }

    @PutMapping("/")
    public Song updateSong(@RequestBody Song song) {
        return songService.update(song);
    }

    @DeleteMapping("/{title}")
    public Song deleteSong(@PathVariable String title) {
        return songService.delete(title);
    }
}
