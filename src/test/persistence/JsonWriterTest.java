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
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Account ac = new Account("My User");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Account ac = new Account("My User");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyAccount");
            writer.open();
            writer.write(ac);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAccount");
            ac = reader.read();
            assertEquals("My User", ac.getUser());
            assertEquals(0, ac.getNumberOfPlaylists());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Account ac = new Account("My User");
            List<Song> jazz = new ArrayList<>();
            List<Song> jams = new ArrayList<>();
            try {
                jazz.add(new Song("Blue in Green", "Miles Davis"));
                jazz.add(new Song("Summertime", "Chet Baker"));
                jams.add(new Song("Mmmh", "Kai"));
            } catch (Exception e) {
                fail("Should not have caught exception");
            }

            ac.addPlaylist(new Playlist("Jazz", jazz));
            ac.addPlaylist(new Playlist("Jams", jams));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAccount");
            writer.open();
            writer.write(ac);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAccount");
            ac = reader.read();
            assertEquals("My User", ac.getUser());
            List<Playlist> playlists = ac.getPlaylists();
            assertEquals(2, playlists.size());
            checkPlaylist("Jazz", jazz, playlists.get(0));
            checkPlaylist("Jams", jams, playlists.get(1));

        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }
}
