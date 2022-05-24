package ui;

import javax.swing.*;

// Used to get user input
public class UserInput {


    public UserInput() {

    }

    // EFFECTS: returns user inputted string
    public static String getString() {
        return (JOptionPane.showInputDialog(null, "Enter String"));
    }

    // EFFECTS: returns user inputted string
    public static String getString(String text) {
        String s = "";
//  used to return user inputted string when inputted string length > 0
//        while (s.length() == 0) {
//            s = JOptionPane.showInputDialog(null, text);
//        }
        s = JOptionPane.showInputDialog(null, text);
        return (s);
    }

}