package org.example.app;

import org.example.menu.LoginMenu;

public class Main {
     static void main(String []args) {
        try {
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.startLoginMenu();

        } catch (Exception exception) {
            System.out.println("[FATAL] " + exception.getMessage());
        }
    }

}

