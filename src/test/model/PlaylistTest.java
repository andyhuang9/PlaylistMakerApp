package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    private Song song1;
    private Song song2;
    private Song song3;
    private ArrayList<Song> testSongs = new ArrayList<>();
    private Playlist testPlaylistEmpty;
    private Playlist testPlaylistTwoSongs;

    @BeforeEach
    public void setUp() {
        try {
            song1 = new Song("T1", "A1");
            song2 = new Song("T2", "A2");
            song3 = new Song("T3", "A3");
        } catch (Exception e) {
            // this should not happen
        }
        testSongs.add(song1);
        testSongs.add(song2);
        testPlaylistEmpty = new Playlist("Playlist Name0");
        testPlaylistTwoSongs = new Playlist("Playlist Name2", testSongs);
    }

    @Test
    public void testRename() {
        assertEquals(testPlaylistEmpty.getName(), "Playlist Name0");
        testPlaylistEmpty.rename("Bops");
        assertEquals(testPlaylistEmpty.getName(), "Bops");
    }

    @Test
    public void testMultipleRenames() {
        assertEquals(testPlaylistEmpty.getName(), "Playlist Name0");
        testPlaylistEmpty.rename("Classical");
        testPlaylistEmpty.rename("Jazz");
        assertEquals(testPlaylistEmpty.getName(), "Jazz");
    }

    @Test
    public void testAddSong() {
        assertEquals(testPlaylistEmpty.getSize(), 0);
        testPlaylistEmpty.addSong(song1);
        assertEquals(testPlaylistEmpty.getSize(), 1);

        assertEquals(testPlaylistTwoSongs.getSize(), 2);
        testPlaylistTwoSongs.addSong(song3);
        assertEquals(testPlaylistTwoSongs.getSize(), 3);
    }

    @Test
    public void testAddSongs() {
        assertEquals(testPlaylistEmpty.getSize(), 0);
        testPlaylistEmpty.addSongs(testSongs);
        assertEquals(testPlaylistEmpty.getSize(), 2);
        assertEquals(testPlaylistEmpty.getSongs().get(0), song1);
        assertEquals(testPlaylistEmpty.getSongs().get(1), song2);
    }

    @Test
    public void testRemoveSong() {
        assertEquals(testPlaylistTwoSongs.getSize(), 2);
        assertEquals(testPlaylistTwoSongs.getSongs().get(0), song1);
        assertEquals(testPlaylistTwoSongs.getSongs().get(1), song2);
        testPlaylistTwoSongs.removeSong("T1", "A1");
        assertEquals(testPlaylistTwoSongs.getSize(), 1);
        assertEquals(testPlaylistTwoSongs.getSongs().get(0), song2);
    }

    @Test
    public void testNoRemoveSong() {
        assertEquals(testPlaylistTwoSongs.getSize(), 2);
        testPlaylistTwoSongs.removeSong("T2", "A1");
        testPlaylistTwoSongs.removeSong("T1", "A2");
        assertEquals(testPlaylistTwoSongs.getSize(), 2);
    }

    @Test
    public void testRemoveMultipleSongs() {
        assertEquals(testPlaylistTwoSongs.getSize(), 2);
        assertEquals(testPlaylistTwoSongs.getSongs().get(0), song1);
        assertEquals(testPlaylistTwoSongs.getSongs().get(1), song2);
        testPlaylistTwoSongs.removeSong("T2", "A2");
        testPlaylistTwoSongs.removeSong("T1", "A1");
        assertEquals(testPlaylistTwoSongs.getSize(), 0);
    }

    @Test
    public void testClearPlaylist() {
        assertEquals(testPlaylistTwoSongs.getSize(), 2);
        testPlaylistTwoSongs.clearPlaylist();
        assertEquals(testPlaylistTwoSongs.getSize(), 0);
    }

    @Test
    public void testEquals() {
        assertNotEquals(testPlaylistEmpty, "String");
        assertNotEquals(testPlaylistEmpty, null);
        assertEquals(testPlaylistEmpty, new Playlist("Playlist Name0"));
        assertEquals(testPlaylistTwoSongs, new Playlist("Playlist Name2"));
    }

    @Test
    public void testHashCode() {
        assertEquals(testPlaylistEmpty.hashCode(), 999891798);
    }
}
