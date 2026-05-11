package com.juki.dao;

import com.juki.db.DatabaseHelper;
import com.juki.model.User;
import java.sql.*;

public class UserDAO {
    
    public User signIn(String username, String password) {
        String sql = "SELECT id, full_name FROM User WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("full_name"), username);
            }
        } catch (SQLException e) {
            System.err.println("UserDAO Error (signIn): " + e.getMessage());
        }
        return null;
    }

    public User signUp(String fullName, String username, String password) {
        String sql = "INSERT INTO User (full_name, username, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, fullName);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return new User(rs.getInt(1), fullName, username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO Error (signUp): " + e.getMessage());
        }
        return null;
    }
}
