package ui;

import exceptions.MissingArtistException;
import exceptions.MissingTitleException;
import model.Account;
import model.Playlist;
import model.Song;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Music Application
public class MusicApp {

    private static final String JSON_STORE = "./data/account";
    private Account myAccount;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the music application
    public MusicApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS:  processes user input
    public void runApp() {
        boolean keepRunning = true;
        String command;

        initialize();

        while (keepRunning) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepRunning = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("Bye bye!");
    }

    // MODIFIES: this
    // EFFECTS:  initializes account
    public void initialize() {
        myAccount = new Account("Andy");
        input = new Scanner(System.in);
    }

    // EFFECTS: displays menu options to user
    public void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tp  -> add playlist");
        System.out.println("\tvp -> view playlists");
        System.out.println("\ta  -> add song to playlist");
        System.out.println("\taa -> add songs to playlist");
        System.out.println("\tr  -> remove song from playlist");
        System.out.println("\tvs -> view songs in playlist");
        System.out.println("\ts  -> save account to file");
        System.out.println("\tl  -> load account from file");
        System.out.println("\tq  -> quit");
    }

    // MODIFIES: this
    // EFFECTS:  processes user command
    public void processCommand(String command) {
        if ("p".equals(command)) {
            doAddPlaylist();
        } else if ("vp".equals(command)) {
            doViewPlaylists();
        } else if ("a".equals(command)) {
            doAddSong();
        } else if ("aa".equals(command)) {
            doAddSongs();
        } else if ("r".equals(command)) {
            doRemoveSong();
        } else if ("vs".equals(command)) {
            doViewSongs();
        } else if ("s".equals(command)) {
            saveAccount();
        } else if ("l".equals(command)) {
            loadAccount();
        } else {
            System.out.println("Selection not valid.");
        }
    }

    // MODIFIES: this
    // EFFECTS:  conducts adding playlist to account
    public void doAddPlaylist() {
        Playlist add;
        String name;

        System.out.println("Enter name of new playlist");
        name = nextLine();

        if (name.length() > 0) {
            add = new Playlist(name);
            myAccount.addPlaylist(add);
            System.out.println("Added new playlist: " + name);
        } else {
            System.out.println("Missing name.");
        }
    }

    // EFFECTS: displays list of playlists currently in account
    public void doViewPlaylists() {
        if (myAccount.getNumberOfPlaylists() == 0) {
            System.out.println("There are currently no playlists on this account.");
        } else {
            System.out.println(myAccount.getUser() + ":");
            for (Playlist p : myAccount.getPlaylists()) {
                System.out.println(p.getName());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  conducts adding a song to playlist
    public void doAddSong() {
        System.out.println("Enter name of playlist to add song to");
        String playlistName = nextLine();
        System.out.println("Enter title of song to add");
        String title = nextLine();
        System.out.println("Enter name of artist to add");
        String artist = nextLine();

        if (checkValidPlaylist(playlistName)) {
            try {
                for (Playlist p : myAccount.getPlaylists()) {
                    if (p.getName().equals(playlistName)) {
                        Song add = new Song(title, artist);
                        p.addSong(add);
                        System.out.println("Added " + title + " by " + artist + " to " + p.getName());
                    }
                }
            } catch (MissingTitleException e) {
                System.out.println("Did not enter title.");
            } catch (MissingArtistException e) {
                System.out.println("Did not enter artist.");
            }
        } else {
            System.out.println("Invalid playlist name");
        }
    }

    // MODIFIES: this
    // EFFECTS:  conducts adding multiple songs to playlist
    public void doAddSongs() {
        ArrayList<Song> add;
        System.out.println("How many songs do you want to add?");
        int num = input.nextInt();

        for (int i = 0; i < num; i++) {
            doAddSong();
        }
    }

    // MODIFIES: this
    // EFFECTS:  conducts removing a song from playlist
    public void doRemoveSong() {
        System.out.println("Enter playlist to remove song from");
        String playlistName = nextLine();
        System.out.println("Enter title of song to remove");
        String title = nextLine();
        System.out.println("Enter name of artist to remove");
        String artist = nextLine();

        if (title.length() == 0) {
            System.out.println("Missing title");
        } else if (artist.length() == 0) {
            System.out.println("Missing artist");
        }
        if (checkValidPlaylist(playlistName)) {
            for (Playlist p : myAccount.getPlaylists()) {
                if (p.getName().equals(playlistName) && p.removeSong(title, artist)) {
                    System.out.println("Removed " + title + " by " + artist + " from " + p.getName());
                }
            }
        } else {
            System.out.println("Invalid playlist name");
        }
    }

    // EFFECTS: displays list of songs currently in playlist
    public void doViewSongs() {
        System.out.println("Enter playlist to view songs from");
        String playlistName = nextLine();

        if (checkValidPlaylist(playlistName)) {
            for (Playlist p : myAccount.getPlaylists()) {
                if (p.getName().equals(playlistName)) {
                    if (p.getSize() == 0) {
                        System.out.println("There are currently no songs in the playlist.");
                    } else {
                        System.out.println(p.getName() + ":");
                        for (Song s : p.getSongs()) {
                            System.out.println(s.getTitle() + " by " + s.getArtist());
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid playlist name");
        }
    }

    // EFFECTS: saves the account to file
    public void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(myAccount);
            jsonWriter.close();
            System.out.println("Saved " + myAccount.getUser() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS:  loads account from file
    private void loadAccount() {
        try {
            myAccount = jsonReader.read();
            System.out.println("Loaded " + myAccount.getUser() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (Exception e) {
            // Unexpected
        }
    }

    // EFFECTS: returns true if account has a playlist of given name, else false
    private boolean checkValidPlaylist(String name) {
        for (Playlist p : myAccount.getPlaylists()) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: reads the correct next line
    private String nextLine() {
        String s = input.nextLine();
        if (s.equals("")) {
            s = input.nextLine();
        }
        return s;
    }

}
