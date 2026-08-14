package org.example.dao.impl;

import org.example.config.DbConnection;
import org.example.dao.UserDao;
import org.example.model.User;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImplementation implements UserDao {

    private static final String SQL_FIND_USERNAME="SELECT * FROM users WHERE username=?";

    @Override
    public User findByUsername(String username) {
        User user = null;
        try(Connection connection = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USERNAME);){

            preparedStatement.setString(1, username);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    user = new User(resultSet.getLong("user_id"),
                                   resultSet.getString("username"),
                                   resultSet.getString("password"),
                                   resultSet.getTimestamp("created_at").toLocalDateTime());
                }
                else {
                    System.out.println("No username Found");
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }
}
