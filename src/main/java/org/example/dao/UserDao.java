package org.example.dao;

import org.example.model.User;

public interface UserDao {
    User findByUsername(String username);
}
