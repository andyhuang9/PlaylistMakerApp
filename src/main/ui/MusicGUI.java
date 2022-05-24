package ui;

import exceptions.MissingArtistException;
import exceptions.MissingTitleException;
import model.Account;
import model.Playlist;
import model.Song;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Graphical User Interface for Music App
// Code inspired by CompositeDrawingEditorComplete from url:
// https://github.students.cs.ubc.ca/CPSC210/CompositeDrawingEditorComplete.git
public class MusicGUI extends JFrame implements ListSelectionListener {

    private static final String JSON_STORE = "./data/account";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static int WIDTH = 700;
    private static int HEIGHT = 500;
    private static int BUTTON_WIDTH = 150;

    private JFrame frame;

    private JButton jbAddPlaylist;
    private JButton jbDeletePlaylist;
    private JButton jbClearPlaylist;
    private JButton jbAddSong;
    private JButton jbRemoveSong;

    private JList list;
    private DefaultListModel listModel;

    private Account myAccount;

    // EFFECTS: runs the music application
    public MusicGUI() {
        super("MusicApplication");
        initializeFields();
        initializeButtons();
        initializeList();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  initializes the fields
    private void initializeFields() {
        String user = UserInput.getString("What is your name?");
        myAccount = new Account(user);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS:  initializes the buttons that the user can use
    private void initializeButtons() {
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(0, 1));
        buttonArea.setSize(new Dimension(BUTTON_WIDTH, HEIGHT));
        add(buttonArea, BorderLayout.WEST);

        jbAddPlaylist = new JButton("Add Playlist");
        jbAddPlaylist.addActionListener(evt -> jbAddPlaylistActionPerformed(evt));
        jbDeletePlaylist = new JButton("Delete Playlist");
        jbDeletePlaylist.addActionListener(evt -> jbDeletePlaylistActionPerformed(evt));
        jbAddSong = new JButton("Add Song");
        jbAddSong.addActionListener(evt -> jbAddSongActionPerformed(evt));
        jbRemoveSong = new JButton("Remove Song");
        jbRemoveSong.addActionListener(evt -> jbRemoveSongActionPerformed(evt));

        buttonArea.add(jbAddPlaylist);
        buttonArea.add(jbDeletePlaylist);
        buttonArea.add(jbAddSong);
        buttonArea.add(jbRemoveSong);

        jbDeletePlaylist.setEnabled(false);
        jbAddSong.setEnabled(false);
        jbRemoveSong.setEnabled(false);
    }

    // MODIFIES: this
    // EFFECTS:  initializes the list for playlists
    private void initializeList() {
        JPanel informationArea = new JPanel();
        informationArea.setLayout(new GridLayout(0, 1));
        informationArea.setSize(new Dimension(300, HEIGHT));
        listModel = new DefaultListModel();
        clearPlaylists();

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        add(informationArea, BorderLayout.EAST);
        informationArea.add(list);
    }

    // MODIFIES: this
    // EFFECTS:  initializes the graphics
    private void initializeGraphics() {
        setLayout(new GridLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        createMenus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS:  initializes the menus
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu accountMenu = new JMenu("Account");
        menuBar.add(accountMenu);

        JMenuItem saveItem = new JMenuItem("Save Account");
        saveItem.addActionListener(evt -> jmSaveItemActionPerformed(evt));
        accountMenu.add(saveItem);
        JMenuItem loadItem = new JMenuItem("Load Account");
        loadItem.addActionListener(evt -> jmLoadItemActionPerformed(evt));
        accountMenu.add(loadItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(evt -> jmQuitItemActionPerformed(evt));
        accountMenu.add(quitItem);

        this.setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS:  adds a playlist after the index of the currently selected playlist
    private void jbAddPlaylistActionPerformed(ActionEvent evt) {
        String playlistName = UserInput.getString("Enter name of new playlist:");

        int index = list.getSelectedIndex(); //get selected index
        if (index == -1) { //no selection, so insert at beginning
            index = 0;
        } else {           //add after the selected item
            index++;
        }

        if (playlistName.length() > 0) {
            myAccount.addPlaylist(index - 1, new Playlist(playlistName));
            listModel.insertElementAt(playlistName, index);
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            setDeletePlaylistButton();
            MessageWindow.displayMessage(this, "Added new playlist: " + playlistName);
        } else {
            MessageWindow.displayMessage(this, "Missing name.");
        }
    }

    // MODIFIES: this
    // EFFECTS:  if valid deletes the currently selected playlist
    private void jbDeletePlaylistActionPerformed(ActionEvent evt) {
        int index = list.getSelectedIndex();
        String playlistName = (String) list.getSelectedValue();

        if (index > 0) {
            listModel.remove(index);
            myAccount.deletePlaylist(playlistName);
            setDeletePlaylistButton();
            MessageWindow.displayMessage(this, "Deleted " + playlistName);
        } else {
            MessageWindow.displayMessage(this, "Invalid selection.");
        }

        int size = listModel.getSize();
        if (size == 1) {
            jbDeletePlaylist.setEnabled(false);
        } else { //Select an index.
            if (index == listModel.getSize()) {
                //removed item in last position
                index--;
            }

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    // MODIFIES: this
    // EFFECTS:  if selection is valid adds song to currently selected playlist
    private void jbAddSongActionPerformed(ActionEvent evt) {
        int index = list.getSelectedIndex();
        String playlistName = (String) list.getSelectedValue();
        String title = UserInput.getString("Enter title of song to add:");
        String artist = UserInput.getString("Enter name of artist to add:");

        if (index > 0) {
            try {
                for (Playlist p : myAccount.getPlaylists()) {
                    if (p.getName().equals(playlistName)) {
                        Song add = new Song(title, artist);
                        p.addSong(add);
                        MessageWindow.displayMessage(this, "Added " + title + " by " + artist + " to " + p.getName());
                    }
                }
            } catch (MissingTitleException e) {
                MessageWindow.displayMessage(this, "Did not enter title.");
            } catch (MissingArtistException e) {
                MessageWindow.displayMessage(this, "Did not enter artist.");
            }
        } else {
            MessageWindow.displayMessage(this, "No playlist selected.");
        }
    }

    // MODIFIES: this
    // EFFECTS:  if selection is valid removes song from currently selected playlist
    private void jbRemoveSongActionPerformed(ActionEvent evt) {
        int index = list.getSelectedIndex();
        String playlistName = (String) list.getSelectedValue();
        String title = UserInput.getString("Enter title of song to remove:");
        String artist = UserInput.getString("Enter name of artist to remove:");

        if (index == 0) {
            MessageWindow.displayMessage(this, "No playlist selected.");
        } else {
            boolean removed = false;
            for (Playlist p : myAccount.getPlaylists()) {
                if (p.getName().equals(playlistName) && p.removeSong(title, artist)) {
                    removed = true;
                    MessageWindow.displayMessage(this, "Removed " + title + " by " + artist + " from " + p.getName());
                }
            }
            if (!removed) {
                MessageWindow.displayMessage(this, "Could not find given song in playlist.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  saves account to file if user selects yes
    private void jmSaveItemActionPerformed(ActionEvent evt) {
        frame = new JFrame("Load");
        String saveString = "Saving may override currently saved account. Are you sure you want to save?";
        int saveInt = JOptionPane.YES_NO_OPTION;
        if (JOptionPane.showConfirmDialog(frame, saveString, "Load", saveInt) == saveInt) {
            try {
                jsonWriter.open();
                jsonWriter.write(myAccount);
                jsonWriter.close();
//                myAccount = new Account(myAccount.getUser());
//                clearPlaylists();
//                setDeletePlaylistButton();
                MessageWindow.displayMessage(this, "Saved " + myAccount.getUser() + " to " + JSON_STORE);
            } catch (FileNotFoundException e) {
                MessageWindow.displayMessage(this, "Unable to write to file: " + JSON_STORE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  loads account from file if user selects yes
    private void jmLoadItemActionPerformed(ActionEvent evt) {
        frame = new JFrame("Load");
        String loadString = "Are you sure you want to override currently loaded account?";
        int loadInt = JOptionPane.YES_NO_OPTION;
        if (JOptionPane.showConfirmDialog(frame, loadString, "Load", loadInt) == loadInt) {
            try {
                clearPlaylists();
                myAccount = jsonReader.read();
                resetUser();
                for (Playlist p : myAccount.getPlaylists()) {
                    listModel.addElement(p.getName());
                }
                setDeletePlaylistButton();
                playAlert("alertGlassyMallet.wav");
                MessageWindow.displayMessage(this, "Loaded " + myAccount.getUser() + " from " + JSON_STORE);
            } catch (IOException e) {
                MessageWindow.displayMessage(this, "Unable to read from file: " + JSON_STORE);
            } catch (Exception e) {
                // Unexpected
            }
        }
    }

    // EFFECTS: quits the application if the user selects yes
    private void jmQuitItemActionPerformed(ActionEvent evt) {
        frame = new JFrame("Quit");
        String quitString = "Quitting may result in losing unsaved data. Are you sure you want to quit?";
        int quitInt = JOptionPane.YES_NO_OPTION;
        if (JOptionPane.showConfirmDialog(frame, quitString, "Quit", quitInt) == quitInt) {
            System.exit(0);
        }
    }

    // MODIFIES: this
    // EFFECTS:  clears all playlists from account
    private void clearPlaylists() {
        listModel.clear();
        listModel.addElement(myAccount.getUser() + "'s Playlists:");
    }

    // MODIFIES: this
    // EFFECTS:  resets the user on the graphic
    private void resetUser() {
        listModel.removeElementAt(0);
        listModel.insertElementAt(myAccount.getUser() + "'s Playlists:", 0);
    }

    // MODIFIES: this
    // EFFECTS:  if there is at least one playlist, enables button, else disables
    private void setDeletePlaylistButton() {
        jbDeletePlaylist.setEnabled(listModel.getSize() > 1);
    }

    // EFFECTS: plays given alert
    private void playAlert(String alertName) {
        String filePath = "./data/" + alertName;
        AudioInputStream audioInputStream;
        Clip clip;
        try {
            File file = new File(filePath);
            audioInputStream = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            MessageWindow.displayMessage(this, "Could not play alert.");
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable add and remove song buttons.
                jbAddSong.setEnabled(false);
                jbRemoveSong.setEnabled(false);

            } else {
                jbAddSong.setEnabled(true);
                jbRemoveSong.setEnabled(true);
            }
        }
    }
}
