package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {


    private Song ClairDeLune;
    private Song SonataNo5;
    private Song WaltzOp64No2;

    private Playlist Jazz;
    private Playlist Bops;
    private Playlist ClassicalHasTwoSongs;
    private Account testAccount;

    @BeforeEach
    public void setUp() {
        try {
            ClairDeLune = new Song("Clair de Lune", "Debussy");
            SonataNo5 = new Song("Sonata No.5 in C Minor", "Beethoven");
            WaltzOp64No2 = new Song("Waltz in C Minor (Op. 64 No. 2)", "Mozart");
        } catch (Exception e) {
            // this shouldn't happen
        }
        Jazz = new Playlist("Jazz");
        Bops = new Playlist("Bops");

        ClassicalHasTwoSongs = new Playlist("Classical");
        ClassicalHasTwoSongs.addSong(SonataNo5);
        ClassicalHasTwoSongs.addSong(WaltzOp64No2);

        testAccount = new Account("Test");
    }

    @Test
    public void testRename() {
        assertEquals(testAccount.getUser(), "Test");
        testAccount.rename("Bob");
        assertEquals(testAccount.getUser(), "Bob");
    }

    @Test
    public void testAddPlaylistOne() {
        assertEquals(0, testAccount.getNumberOfPlaylists());
        testAccount.addPlaylist(Jazz);
        assertEquals(1, testAccount.getNumberOfPlaylists());
        assertEquals(testAccount.getPlaylists().get(0), Jazz);
    }

    @Test
    public void testAddPlaylistTwo() {
        assertEquals(0, testAccount.getNumberOfPlaylists());
        testAccount.addPlaylist(Jazz);
        testAccount.addPlaylist(Bops);
        assertEquals(2, testAccount.getNumberOfPlaylists());
        assertEquals(testAccount.getPlaylists().get(0), Jazz);
        assertEquals(testAccount.getPlaylists().get(1), Bops);
    }

    @Test
    public void testAddPlaylistAtIndex() {
        assertEquals(0, testAccount.getNumberOfPlaylists());
        testAccount.addPlaylist(Jazz);
        testAccount.addPlaylist(Bops);
        assertEquals(2, testAccount.getNumberOfPlaylists());

        testAccount.addPlaylist(1, new Playlist("Chill"));
        assertEquals(3, testAccount.getNumberOfPlaylists());
        assertEquals(testAccount.getPlaylists().get(0), Jazz);
        assertEquals(testAccount.getPlaylists().get(1), new Playlist("Chill"));
        assertEquals(testAccount.getPlaylists().get(2), Bops);
    }

    @Test
    public void testMakeNewPlaylistOne() {
        assertEquals(0, testAccount.getNumberOfPlaylists());
        testAccount.makeNewPlaylist("Kpop");
        assertEquals(1, testAccount.getNumberOfPlaylists());
        assertEquals(testAccount.getPlaylists().get(0).getName(), "Kpop");
    }

    @Test
    public void testDeletePlaylist() {
        testAccount.addPlaylist(Jazz);
        testAccount.addPlaylist(Bops);
        assertEquals(2, testAccount.getNumberOfPlaylists());

        testAccount.deletePlaylist("Doesn't exist");
        assertEquals(2, testAccount.getNumberOfPlaylists());

        testAccount.deletePlaylist("Bops");
        assertEquals(1, testAccount.getNumberOfPlaylists());
        assertEquals(testAccount.getPlaylists().get(0), Jazz);

        testAccount.deletePlaylist("Jazz");
        assertEquals(0, testAccount.getNumberOfPlaylists());
    }

    @Test
    public void testAddSongsToPlaylist() {
        testAccount.addPlaylist(Jazz);
        testAccount.addPlaylist(ClassicalHasTwoSongs);
        assertEquals(2, testAccount.getNumberOfPlaylists());
        assertEquals(0, testAccount.getPlaylists().get(0).getSize());
        assertEquals(2, testAccount.getPlaylists().get(1).getSize());

        testAccount.addSongToPlaylist("Doesn't exist", ClairDeLune);
        assertEquals(0, testAccount.getPlaylists().get(0).getSize());
        assertEquals(2, testAccount.getPlaylists().get(1).getSize());

        testAccount.addSongToPlaylist("Classical", ClairDeLune);
        assertEquals(0, testAccount.getPlaylists().get(0).getSize());
        assertEquals(3, testAccount.getPlaylists().get(1).getSize());
        assertEquals(ClairDeLune, testAccount.getPlaylists().get(1).getSongs().get(2));

        testAccount.addSongToPlaylist("Classical", SonataNo5);
        assertEquals(0, testAccount.getPlaylists().get(0).getSize());
        assertEquals(4, testAccount.getPlaylists().get(1).getSize());
        assertEquals(SonataNo5, testAccount.getPlaylists().get(1).getSongs().get(3));
    }
}
