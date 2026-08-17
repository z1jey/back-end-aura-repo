package org.example;
import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.dao.impl.AccountDaoImplementation;
import org.example.dao.impl.TransactionDaoImplementation;
import org.example.service.AccountService;
import org.example.service.TransactionService;
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
        TransactionDao transactionDao = new TransactionDaoImplementation();
        TransactionService transactionService = new TransactionService(accountDao, transactionDao, scanner, accountService);
//        accountService.createAccountService()
//        accountService.balanceInquiry();
//            accountService.listAccounts();
        transactionService.deposit();
    }
}
