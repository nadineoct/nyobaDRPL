package com.juki.dao;

import com.juki.db.DatabaseHelper;
import com.juki.model.JournalEntry;
import com.juki.model.Photo;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JournalEntryDAO {

    public boolean save(JournalEntry entry) {
        String sql = "INSERT INTO JournalEntry (category, title, description, trigger, target, date, time, photo_id, user_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, entry.getCategory());
            pstmt.setString(2, entry.getTitle());
            pstmt.setString(3, entry.getDescription());
            pstmt.setString(4, entry.getTrigger());
            pstmt.setString(5, entry.getTarget());
            pstmt.setString(6, entry.getDate().toString());
            pstmt.setString(7, entry.getTime().toString());
            
            if (entry.getPhoto() != null && entry.getPhoto().getId() != null) {
                pstmt.setInt(8, entry.getPhoto().getId());
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }
            
            pstmt.setInt(9, entry.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entry.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("JournalEntryDAO Error (save): " + e.getMessage());
        }
        return false;
    }

    public List<JournalEntry> getAllByUserId(int userId) {
        List<JournalEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM JournalEntry WHERE user_id = ? ORDER BY date DESC, time DESC";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                JournalEntry entry = mapResultSetToEntry(rs);
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("JournalEntryDAO Error (getAllByUserId): " + e.getMessage());
        }
        return entries;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM JournalEntry WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("JournalEntryDAO Error (delete): " + e.getMessage());
            return false;
        }
    }

    private JournalEntry mapResultSetToEntry(ResultSet rs) throws SQLException {
        JournalEntry entry = new JournalEntry();
        entry.setId(rs.getInt("id"));
        entry.setCategory(rs.getString("category"));
        entry.setTitle(rs.getString("title"));
        entry.setDescription(rs.getString("description"));
        entry.setTrigger(rs.getString("trigger"));
        entry.setTarget(rs.getString("target"));
        entry.setDate(LocalDate.parse(rs.getString("date")));
        entry.setTime(LocalTime.parse(rs.getString("time")));
        entry.setUserId(rs.getInt("user_id"));
        
        int photoId = rs.getInt("photo_id");
        if (!rs.wasNull()) {
            // Kita bisa melakukan join atau fetch foto terpisah jika perlu
            Photo photo = new Photo();
            photo.setId(photoId);
            entry.setPhoto(photo);
        }
        
        return entry;
    }
}
