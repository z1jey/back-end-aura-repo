package org.example.dao.impl;

import org.example.config.DbConnection;
import org.example.dao.UserDao;
import org.example.model.User;

import java.sql.*;

public class UserDaoImplementation implements UserDao {

    private static final String SQL_FIND_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SQL_CREATE_USER = "INSERT INTO users (admin_name, username, password) VALUES (?, ?, ?)";

    @Override
    public User findByUsername(String username) throws SQLException {
        User user = null;
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USERNAME)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getLong("user_id"),
                            resultSet.getString("admin_name"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );
                }
                return user;
            }
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to find username.");
            System.out.println("[ERROR] " + sqlException.getMessage());
        }
        return user;
    }

    @Override
    public void createUser(User user) throws SQLException {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getAdminName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {

                if (keys.next()) {
                    user.setUserId(keys.getLong(1));
                }
            }

        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to create admin account.");
            System.out.println("[ERROR] " + sqlException.getMessage());
            throw sqlException;
        }
    }
}