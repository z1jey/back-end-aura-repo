package org.example.dao.impl;

import org.example.config.DbConnection;
import org.example.dao.AccountDao;
import org.example.model.Account;
import org.example.model.AccountStatus;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImplementation implements AccountDao {

    private static final String SQL_CREATE_ACCOUNT =
            "INSERT INTO accounts(account_number, first_name, last_name, contact_number, balance) VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ACCOUNT =
            "SELECT * FROM accounts WHERE account_number = ?";
    private static final String SQL_FIND_ALL =
            "SELECT * FROM accounts ORDER BY created_at ASC";
    private static final String SQL_UPDATE_BALANCE =
            "UPDATE accounts SET balance = ? WHERE account_number = ?";
    private static final String SQL_DELETE_ACCOUNT =
            "UPDATE accounts SET account_status = ? WHERE account_number = ?";
    private static final String SQL_FIND_ARCHIVED =
            "SELECT * FROM accounts WHERE account_status = 'ARCHIVED' ORDER BY created_at ASC";
    private static final String SQL_FIND_ACTIVE =
            "SELECT * FROM accounts WHERE account_status = 'ACTIVE' ORDER BY created_at ASC";

    @Override
    public void createAccount(Account account) throws SQLException {
        try(Connection connection = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_ACCOUNT, Statement.RETURN_GENERATED_KEYS)){

            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountFirstName());
            preparedStatement.setString(3, account.getAccountLastName());
            preparedStatement.setString(4, account.getAccountContactNumber());
            preparedStatement.setBigDecimal(5, account.getBalance());
            preparedStatement.executeUpdate();

            try(ResultSet keys = preparedStatement.getGeneratedKeys()){
                if(keys.next()){
                    account.setAccountId(keys.getLong(1));
                }
            }
//            System.out.println("Account Created Successfully!");
//            System.out.println("Account Number: " + account.getAccountNumber());
//            System.out.println("Account Holder Name: " + account.getAccountFirstName() + " " + account.getAccountLastName());
//            System.out.println("Account Contact Number: " + account.getAccountContactNumber());
//            System.out.println("Account with Initial Balance(₱): " + account.getBalance());

        } catch (SQLException sqlException) {
            System.out.println("Failed to Create an Account");
            System.out.println("[ERROR] " + sqlException.getMessage());
            throw sqlException;
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) throws SQLException {
        try(Connection connection = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ACCOUNT)){
            preparedStatement.setString(1, accountNumber);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            System.out.println("Failed to find the account");
            System.out.println("[ERROR] " + exception.getMessage());
            throw exception;
        }

    }

    @Override
    public List<Account> findAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try(Connection connection = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)){
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accounts.add(mapRow(resultSet));
            }
            return accounts;
        } catch (SQLException sqlException) {
            System.out.println("Failed to list all the accounts");
            System.out.println("[ERROR] " + sqlException.getMessage());
            throw sqlException;
        }
    }

    @Override
    public List<Account> findArchivedAccounts() throws SQLException {

        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DbConnection.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ARCHIVED)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(mapRow(resultSet));
            }

            return accounts;
        } catch (SQLException sqlException) {

            System.out.println("[ERROR] Failed to retrieve archived accounts.");
            System.out.println("[ERROR] " + sqlException.getMessage());
            throw sqlException;
        }
    }

    @Override
    public void updateBalance(String accountNumber, BigDecimal balance) throws SQLException {
        try(Connection connection = DbConnection.getConnection()){
            updateBalance(connection, accountNumber, balance);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update balance for account number " + accountNumber);
            System.out.println("[ERROR] " + sqlException.getMessage());
            throw sqlException;
        }
    }

    @Override
    public void updateBalance(Connection connection, String accountNumber, BigDecimal balance) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BALANCE)){
            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setString(2, accountNumber);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Balance updated for account " + accountNumber);
            }
        }
    }

    @Override
    public void deleteAccount(String accountNumber) throws SQLException {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ACCOUNT)) {

            preparedStatement.setString(1, AccountStatus.ARCHIVED.name());
            preparedStatement.setString(2, accountNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException(
                        "[WARNING] No account found to delete: " + accountNumber
                );
            }

        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to delete account: " + accountNumber);
            System.out.println("[ERROR] " + sqlException.getMessage());
            throw sqlException;
        }
    }

    @Override
    public List<Account> findActiveAccounts() throws SQLException {

        List<Account> accounts = new ArrayList<>();

        try ( Connection connection = DbConnection.getConnection();
                PreparedStatement preparedStatement =  connection.prepareStatement(SQL_FIND_ACTIVE)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(mapRow(resultSet));
            }

            return accounts;
        } catch (SQLException sqlException) {

            System.out.println("[ERROR] Failed to retrieve active accounts.");
            System.out.println("[ERROR] " + sqlException.getMessage());

            throw sqlException;
        }
    }

    //helpers
    private Account mapRow(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountId(resultSet.getLong("account_id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setAccountFirstName(resultSet.getString("first_name"));
        account.setAccountLastName(resultSet.getString("last_name"));
        account.setAccountContactNumber(resultSet.getString("contact_number"));
        account.setBalance(resultSet.getBigDecimal("balance"));

        account.setAccountStatus(AccountStatus.valueOf(resultSet.getString("account_status")));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            account.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            account.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return account;
    }
}
