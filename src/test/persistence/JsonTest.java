package persistence;

import model.Playlist;
import model.Song;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JsonTest {

    protected void checkPlaylist(String name, List<Song> s, Playlist pl) {
        assertEquals(name, pl.getName());
        for (int i = 0; i < pl.getSize(); i++) {
            assertEquals(s.get(i).getTitle(), pl.getSongs().get(i).getTitle());
            assertEquals(s.get(i).getArtist(), pl.getSongs().get(i).getArtist());
        }
    }
}
