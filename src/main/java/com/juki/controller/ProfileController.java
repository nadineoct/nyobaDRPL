package com.juki.controller;

import com.juki.db.DatabaseHelper;
import com.juki.model.User;
import com.juki.model.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileController {

    public User getActiveUser() {
        // Mengambil data user yang saat ini sedang login dari session
        return UserSession.getInstance().getActiveUser();
    }

    public void logout(Runnable onLogoutAction) {
        System.out.println("Memproses logout, menghapus data session...");
        UserSession.getInstance().clearSession();
        
        if (onLogoutAction != null) {
            onLogoutAction.run(); // Callback mengarahkan kembali ke halaman login
        }
    }

    public void updateProfilePhoto(int userId, String imagePath) {
        String sql = "UPDATE User SET profile_image_path = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imagePath);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui foto profil: " + e.getMessage());
        }
    }
}