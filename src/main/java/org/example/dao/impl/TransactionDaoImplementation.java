package org.example.dao.impl;

import org.example.config.DbConnection;
import org.example.dao.TransactionDao;
import org.example.model.Transaction;
import org.example.model.TransactionType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImplementation implements TransactionDao {

    private static final String SQL_INSERT_TRANSACTION =
            "INSERT INTO transactions(account_number, transaction_type, amount, balance_after, reference_number, remarks)" +
                    "VALUES(?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ACCOUNT =
            "SELECT * FROM transactions WHERE account_number = ? ORDER BY created_at DESC";

    private static final String SQL_FIND_RECENT =
            "SELECT * FROM transactions WHERE account_number = ? ORDER BY created_at DESC LIMIT = ?";

    @Override
    public void save(Transaction transaction) throws SQLException {
        try(Connection connection = DbConnection.getConnection()) {
            save(connection, transaction);
        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to save Transaction: " + transaction.getReferenceNumber());
            System.out.println("[ERROR] " + exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void save(Connection connection, Transaction transaction) throws SQLException {
        try(Connection connection1 = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_TRANSACTION)){
            preparedStatement.setString(1, transaction.getAccountNumber());
            preparedStatement.setString(2, transaction.getTransactionType().name());
            preparedStatement.setBigDecimal(3, transaction.getAmount());
            preparedStatement.setBigDecimal(4, transaction.getBalanceAfter());
            preparedStatement.setString(5, transaction.getReferenceNumber());
            preparedStatement.setString(6, transaction.getRemarks());

            try(ResultSet keys = preparedStatement.getGeneratedKeys()){
                if (keys.next()) {
                    transaction.setTransactionId(keys.getLong(1));
                }
            }

            System.out.println("[SUCCESS] Transaction have been saved: " + transaction.getReferenceNumber());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to save transaction");
            System.out.println("[ERROR] " + sqlException.getMessage());
        }
    }

    @Override
    public List<Transaction> findByAccountTransaction(String accountNumber) throws SQLException {
        List<Transaction> results = new ArrayList<>();
        try(Connection connection = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ACCOUNT)){

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    results.add(mapRow(resultSet));
                }
            }
        }
        return List.of();
    }

    @Override
    public List<Transaction> findByRecentTransaction(String accountNumber, int limit) throws SQLException {
        List<Transaction> results = new ArrayList<>();

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_RECENT)) {

            ps.setString(1, accountNumber);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
            return results;
        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to fetch recent transactions for: " + accountNumber);
            System.out.println("[ERROR] " + exception.getMessage());
            throw exception;
        }
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction txn = new Transaction();
        txn.setTransactionId(rs.getLong("transaction_id"));
        txn.setAccountNumber(rs.getString("account_number"));
        txn.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        txn.setAmount(rs.getBigDecimal("amount"));
        txn.setBalanceAfter(rs.getBigDecimal("balance_after"));
        txn.setReferenceNumber(rs.getString("reference_number"));
        txn.setRemarks(rs.getString("remarks"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            txn.setCreated_at(createdAt.toLocalDateTime());
        }

        return txn;
    }
}

