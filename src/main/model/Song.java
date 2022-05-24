package model;

import exceptions.MissingArtistException;
import exceptions.MissingTitleException;
import org.json.JSONObject;
import persistence.Writable;

// Represents a song having a title and an artist
public class Song implements Writable {

    private String title;
    private String artist;


    // EFFECTS: if title length == 0 throws MissingTitleException, if artist length == 0 throws MissingArtistException,
    //          else sets song title and artist as title and artist respectively
    public Song(String title, String artist) throws MissingTitleException, MissingArtistException {
        if (title.length() == 0) {
            throw new MissingTitleException();
        }
        if (artist.length() == 0) {
            throw new MissingArtistException();
        }
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    // MODIFIES: this
    // EFFECTS:  changes song title to newTitle
    public void changeTitle(String newTitle) throws MissingTitleException {
        if (newTitle.length() == 0) {
            throw new MissingTitleException();
        }
        this.title = newTitle;
    }

    // MODIFIES: this
    // EFFECTS:  changes song artist to newArtist
    public void changeArtist(String newArtist) throws MissingArtistException {
        if (newArtist.length() == 0) {
            throw new MissingArtistException();
        }
        this.artist = newArtist;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("artist", artist);
        return json;
    }

}
