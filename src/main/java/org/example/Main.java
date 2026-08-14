package org.example;
import org.example.dao.AccountDao;
import org.example.dao.impl.AccountDaoImplementation;
import org.example.model.Account;
import org.example.model.User;
import org.example.dao.UserDao;
import org.example.dao.impl.UserDaoImplementation;
import org.example.service.AccountService;
import org.example.service.LoginService;

import java.util.Scanner;

public class Main {
    public static void main(String []args) {
//        UserDao userDao = new UserDaoImplementation();
//
//        User user = userDao.findByUsername("admin");
//
//        if(user != null){
//            System.out.println(user.getUserId());
//            System.out.println(user.getUsername());
//            System.out.println(user.getPassword());
//        }
//        else {
//            System.out.println("No username found!");
//        }
//        UserDao userDao = new UserDaoImplementation();
//        LoginService loginService = new LoginService(userDao, scanner);
//        loginService.login();

        Scanner scanner = new Scanner(System.in);
        AccountDao accountDao = new AccountDaoImplementation();
        AccountService accountService = new AccountService(accountDao, scanner);

        accountService.createAccountService();
    }
}
