package org.example.dao;
import org.example.model.User;
import java.sql.SQLException;

public interface UserDao {
    User findByUsername(String username) throws SQLException;
    void createUser(User user) throws SQLException;
}
