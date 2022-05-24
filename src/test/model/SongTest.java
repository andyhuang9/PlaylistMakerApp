package model;

import exceptions.MissingArtistException;
import exceptions.MissingTitleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    private Song testSong;

    @BeforeEach
    public void setUp() {
        try {
            testSong = new Song("Song Name", "Song Artist");
        } catch (Exception e) {
            // this shouldn't happen
        }
    }

    @Test
    public void testNewLegalSong() {
        try {
            Song legalSong = new Song("title > 0", "artist > 0");
        } catch (MissingTitleException e) {
            fail("Should not have caught MissingTitleException");
        } catch (MissingArtistException e) {
            fail("Should not have caught MissingArtistException");
        }
    }

    @Test
    public void testNewSongNoTitle() {
        try {
            Song noTitleSong = new Song("", "artist > 0");
            fail("Should have caught MissingTitleException");
        } catch (MissingTitleException e) {
            // Expected
        } catch (MissingArtistException e) {
            fail("Should not have caught MissingArtistException");
        }
    }

    @Test
    public void testNewSongNoArtist() {
        try {
            Song noArtistSong = new Song("title > 0", "");
            fail("Should have caught MissingArtistException");
        } catch (MissingTitleException e) {
            fail("Should not have caught MissingTitleException");
        } catch (MissingArtistException e) {
            // Expected
        }
    }

    @Test
    public void testChangeTitleToWithLength() {
        assertEquals(testSong.getTitle(), "Song Name");
        try {
            testSong.changeTitle("Spain");
        } catch (Exception e) {
            fail("Should not have caught any exceptions");
        }
        assertEquals(testSong.getTitle(), "Spain");
    }

    @Test
    public void testChangeTitleToNoLength() {
        assertEquals(testSong.getTitle(), "Song Name");
        try {
            testSong.changeTitle("");
            fail("Should have caught exception");
        } catch (MissingTitleException e) {
            // Expected
        }
        assertEquals(testSong.getTitle(), "Song Name");
    }

    @Test
    public void testChangeArtistToWithLength() {
        assertEquals(testSong.getArtist(), "Song Artist");
        try {
            testSong.changeArtist("Chick Corea");
        } catch (Exception e) {
            fail("Should not have caught any exceptions");
        }
        assertEquals(testSong.getArtist(), "Chick Corea");
    }

    @Test
    public void testChangeArtistToNoLength() {
        assertEquals(testSong.getArtist(), "Song Artist");
        try {
            testSong.changeArtist("");
            fail("Should have caught exception");
        } catch (Exception e) {
            // Expected
        }
        assertEquals(testSong.getArtist(), "Song Artist");
    }
}