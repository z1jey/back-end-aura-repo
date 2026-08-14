package org.example.service;
import org.example.model.User;
import org.example.dao.UserDao;
import java.io.Console;
import java.util.Scanner;

public class LoginService {

    private final UserDao userDao;
    private final Scanner scanner;

    public LoginService(UserDao userDao, Scanner scanner){
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public boolean login() {
        Console console = System.console();

        System.out.print("Enter admin Username: ");
        String adminUsername = scanner.nextLine().trim();

        System.out.print("Enter admin Password: ");
        String adminPassword = scanner.nextLine().trim();

        User user = userDao.findByUsername(adminUsername);

        if(user != null){
            if(adminPassword.equals(user.getPassword())){
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Wrong Password");
                return false;
            }
        } else {
            System.out.println("No account Found!");
        }
        return false;
    }


}
