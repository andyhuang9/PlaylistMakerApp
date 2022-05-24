package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents an account with a user and a list of playlists
public class Account implements Writable {

    private String user;
    private List<Playlist> playlists;

    // REQUIRES: name has non-zero length
    // EFFECTS:  name is set to accountName
    public Account(String accountName) {
        user = accountName;
        playlists = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

//    public Playlist getPlaylist(String playlistName) {
//        Playlist toFind = new Playlist(playlistName);
//        for (Playlist p : playlists) {
//            if (p.equals(toFind)) {
//                return p;
//            }
//        }
//    }

    public int getNumberOfPlaylists() {
        return playlists.size();
    }

    // REQUIRES: newName has non-zero length; newName is not the same
    //           as the current account name
    // MODIFIES: this
    // EFFECTS:  changes account name to newName
    public void rename(String newName) {
        this.user = newName;
    }

    // REQUIRES: p does not have the same name as a playlist in playlists
    // MODIFIES: this
    // EFFECTS:  adds p to playlists
    public void addPlaylist(Playlist p) {
        playlists.add(p);
    }

    // REQUIRES: p does not have the same name as a playlist in playlists
    // MODIFIES: this
    // EFFECTS:  adds p to playlists at index
    public void addPlaylist(int index, Playlist p) {
        playlists.add(index, p);
    }

    // REQUIRES: playlistName is non-zero length; there is no playlist in the account
    //           with the same name
    // MODIFIES: this
    // EFFECTS:  adds new playlist with name playlistName to playlists
    public void makeNewPlaylist(String playlistName) {
        playlists.add(new Playlist(playlistName));
    }

    // REQUIRES: playlistName is non-zero length
    // MODIFIES: this
    // EFFECTS:  removes the playlist with name playlistName from playlists
    public void deletePlaylist(String playlistName) {
        for (Playlist p : playlists) {
            if (p.getName().equals(playlistName)) {
                playlists.remove(p);
                break;
            }
        }

    }

    // REQUIRES: playlistName is non-zero length
    // MODIFIES: this
    // EFFECTS:  adds the song to the correct playlist
    public void addSongToPlaylist(String playlistName, Song song) {
        for (Playlist p : playlists) {
            if (p.getName().equals(playlistName)) {
                p.addSong(song);
            }
        }

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("user", user);
        json.put("playlists", playlistsToJson());
        return json;
    }

    // EFFECTS: returns playlists in this account as a JSON array
    private JSONArray playlistsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Playlist p : playlists) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }

}
