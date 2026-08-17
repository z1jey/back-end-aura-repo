package org.example.model;
import java.time.LocalDateTime;

public class User {
    private long userId;
    private String adminName;
    private String username;
    private String password;
    private LocalDateTime createdAt;

    public User(String adminName, String username, String password) {
        this.adminName = adminName;
        this.username = username;
        this.password = password;
    }

    public User( long userId, String adminName,
            String username,
            String password,
            LocalDateTime createdAt) {

        this.userId = userId;
        this.adminName = adminName;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}






