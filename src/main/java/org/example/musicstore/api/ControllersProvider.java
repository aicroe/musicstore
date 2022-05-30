package org.example.musicstore.api;

import org.example.musicstore.core.album.AlbumRepository;
import org.example.musicstore.core.album.AlbumService;
import org.example.musicstore.core.album.AlbumValidator;
import org.example.musicstore.core.album.ConcreteAlbumService;
import org.example.musicstore.core.artist.ArtistRepository;
import org.example.musicstore.core.artist.ArtistService;
import org.example.musicstore.core.artist.ArtistValidator;
import org.example.musicstore.core.song.ConcreteSongService;
import org.example.musicstore.core.song.SongService;
import org.example.musicstore.core.song.SongValidator;
import org.example.musicstore.persistence.album.AlbumAdapter;
import org.example.musicstore.persistence.album.AlbumDao;
import org.example.musicstore.persistence.artist.ArtistAdapter;
import org.example.musicstore.persistence.artist.ArtistDao;
import org.example.musicstore.persistence.song.SongAdapter;
import org.example.musicstore.persistence.song.SongDao;
import org.example.musicstore.core.artist.ConcreteArtistService;
import org.example.musicstore.core.song.SongRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllersProvider {

    @Bean
    public ArtistRepository getArtistRepository(ArtistDao artistDao) {
        return new ArtistAdapter(artistDao);
    }

    @Bean
    public SongRepository getSongRepository(SongDao songDao, ArtistDao artistDao) {
        return new SongAdapter(songDao, artistDao);
    }

    @Bean
    public AlbumRepository getAlbumRepository(AlbumDao albumDao, ArtistDao artistDao, SongDao songDao) {
        return new AlbumAdapter(albumDao, artistDao, songDao);
    }

    @Bean
    public ArtistService getArtistService(ArtistDao artistDao, SongDao songDao, AlbumDao albumDao) {
        return new ConcreteArtistService(
            getArtistRepository(artistDao),
            getSongRepository(songDao, artistDao),
            getAlbumRepository(albumDao, artistDao, songDao),
            new ArtistValidator()
        );
    }

    @Bean
    public SongService getSongService(ArtistDao artistDao, SongDao songDao) {
        return new ConcreteSongService(
            getSongRepository(songDao, artistDao),
            getArtistRepository(artistDao),
            new SongValidator()
        );
    }

    @Bean
    public AlbumService getAlbumService(AlbumDao albumDao, ArtistDao artistDao, SongDao songDao) {
        return new ConcreteAlbumService(
            getAlbumRepository(albumDao, artistDao, songDao),
            getArtistRepository(artistDao),
            getSongRepository(songDao, artistDao),
            new AlbumValidator()
        );
    }
}
