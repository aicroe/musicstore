package org.example.musicstore.api;

import org.example.musicstore.core.artist.Artist;
import org.example.musicstore.core.artist.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/artist")
@RestController
public class ArtistController {

    private ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/all")
    public List<Artist> getAll() {
        return artistService.getAll();
    }

    @GetMapping("/{name}")
    public Artist getArtist(@PathVariable String name) {
        return artistService.get(name);
    }

    @PostMapping("/")
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.create(artist);
    }

    @PutMapping("/")
    public Artist updateArtist(@RequestBody Artist artist) {
        return artistService.update(artist);
    }

    @DeleteMapping("/{name}")
    public Artist deleteArtist(@PathVariable String name) {
        return artistService.delete(name);
    }
}
