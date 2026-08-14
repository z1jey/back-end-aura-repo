package org.example.dao;

import org.example.model.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionDao {
    void save(Transaction transaction) throws SQLException;
    void save(Connection connection, Transaction transaction) throws SQLException;
    List<Transaction> findByAccountTransaction(String accountNumber) throws SQLException;
    List<Transaction> findByRecentTransaction(String accountNumber, int limit) throws SQLException;
}
