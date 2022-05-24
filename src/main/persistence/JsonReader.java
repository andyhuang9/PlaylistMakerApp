package persistence;

import exceptions.MissingArtistException;
import exceptions.MissingTitleException;
import model.Account;
import model.Playlist;
import model.Song;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads account from JSON data stored in file
// TODO citation: code obtained from JsonSerializationDemo
//                url: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads account from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account read() throws IOException, MissingArtistException, MissingTitleException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses account from JSON object and returns it
    private Account parseAccount(JSONObject jsonObject) throws MissingArtistException, MissingTitleException {
        String user = jsonObject.getString("user");
        Account ac = new Account(user);
        addPlaylists(ac, jsonObject);
        return ac;
    }

    // MODIFIES: ac
    // EFFECTS: parses playlists from JSON object and adds them to account
    private void addPlaylists(Account ac, JSONObject jsonObject) throws MissingArtistException, MissingTitleException {
        JSONArray jsonArray = jsonObject.getJSONArray("playlists");
        for (Object json : jsonArray) {
            JSONObject nextPlaylist = (JSONObject) json;
            addPlaylist(ac, nextPlaylist);
        }
    }

    // MODIFIES: ac
    // EFFECTS: parses playlist from JSON object and adds it to account
    private void addPlaylist(Account ac, JSONObject jsonObject) throws MissingArtistException, MissingTitleException {
        String name = jsonObject.getString("name");
        Playlist playlist = new Playlist(name);
        addSongs(playlist, jsonObject);
        ac.addPlaylist(playlist);
    }

    // MODIFIES: pl
    // EFFECTS: parses songs from JSON object and adds them to playlist
    private void addSongs(Playlist pl, JSONObject jsonObject) throws MissingArtistException, MissingTitleException {
        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            addSong(pl, nextSong);
        }
    }

    // MODIFIES: pl
    // EFFECTS: parses song from JSON object and adds it to playlist
    private void addSong(Playlist pl, JSONObject jsonObject) throws MissingArtistException, MissingTitleException {
        String title = jsonObject.getString("title");
        String artist = jsonObject.getString("artist");
        Song song = new Song(title, artist);
        pl.addSong(song);

    }
}
