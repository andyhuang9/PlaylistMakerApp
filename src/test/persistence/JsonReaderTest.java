package persistence;

import model.Account;
import model.Playlist;
import model.Song;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// TODO citation: code obtained from JsonSerializationDemo
//                url: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account ac = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (Exception e) {
            fail("IOException expected");
        }
    }

    @Test
    void testReaderEmptyAccount() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAccount");
        try {
            Account ac = reader.read();
            assertEquals("My User", ac.getUser());
            assertEquals(0, ac.getNumberOfPlaylists());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (Exception e) {
            fail("Exception unexpected");
        }
    }

    @Test
    void testReaderGeneralAccount() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAccount");
        try {
            Account ac = reader.read();
            assertEquals("My User", ac.getUser());
            List<Playlist> playlists = ac.getPlaylists();
            assertEquals(2, playlists.size());
            List<Song> jazz = new ArrayList<>();
            List<Song> jams = new ArrayList<>();
            try {
                jazz.add(new Song("Blue in Green", "Miles Davis"));
                jazz.add(new Song("Summertime", "Chet Baker"));
                jams.add(new Song("Mmmh", "Kai"));
            } catch (Exception e) {
                fail("Should not have caught exception");
            }
            checkPlaylist("Jazz", jazz, playlists.get(0));
            checkPlaylist("Jams", jams, playlists.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (Exception e) {
            fail("Exception unexpected");
        }
    }
}