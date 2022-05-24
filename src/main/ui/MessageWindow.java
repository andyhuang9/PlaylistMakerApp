package ui;

import javax.swing.*;
import java.awt.*;

// Used to display messages to the user
public class MessageWindow {

    public MessageWindow() {

    }

    // EFFECTS: displays message to the user
    public static void displayMessage(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
}
