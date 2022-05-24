package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// Represents a playlist with a name and a list of songs
public class Playlist implements Writable {

    private String name;
    private List<Song> songs;

    // REQUIRES: name has non-zero length
    // EFFECTS:  name on playlist is set to playlistName
    public Playlist(String playlistName) {
        this.name = playlistName;
        songs = new ArrayList<>();
    }

    // REQUIRES: name has non-zero length
    // EFFECTS:  name on playlist is set to playlistName; the
    //           songs in songsToAdd are added to songs on playlist
    public Playlist(String playlistName, List<Song> songsToAdd) {
        this.name = playlistName;
        songs = new ArrayList<>();
        songs.addAll(songsToAdd);
    }

    public String getName() {
        return this.name;
    }

    public List<Song> getSongs() {
        return this.songs;
    }

    public int getSize() {
        return this.songs.size();
    }

    // REQUIRES: newName has non-zero length; newName is not the same
    //           as the current playlist name
    // MODIFIES: this
    // EFFECTS:  changes playlist name to newName
    public void rename(String newName) {
        this.name = newName;
    }

    // MODIFIES: this
    // EFFECTS:  adds song to playlist songs
    public void addSong(Song song) {
        this.songs.add(song);
    }

    // REQUIRES: songsToAdd is not an empty list
    // MODIFIES: this
    // EFFECTS:  adds all songs in songsToAdd to playlist songs
    public void addSongs(ArrayList<Song> songsToAdd) {
        this.songs.addAll(songsToAdd);
    }

    // REQUIRES: title and artist are non-zero length
    // MODIFIES: this
    // EFFECTS:  removes first instance of song with the given title and artist
    public boolean removeSong(String title, String artist) {
        for (Song s : this.songs) {
            if (s.getTitle().equals(title) && s.getArtist().equals(artist)) {
                this.songs.remove(s);
                return true;
            }
        }
        return false;
    }

    // REQUIRES: playlist is non-empty
    // MODIFIES: this
    // EFFECTS:  clears all songs in playlist
    public void clearPlaylist() {
        this.songs.clear();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("songs", songsToJson());
        return json;
    }

    // EFFECTS: returns songs in this playlist as a JSON array
    private JSONArray songsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Song s : songs) {
            jsonArray.put(s.toJson());
        }
        return jsonArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        return name.equals(playlist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
