package com.juki.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DatabaseHelper {
    private static final String DB_DIR = "data";
    private static final String DB_NAME = "juki.db";
    private static final String URL = "jdbc:sqlite:" + DB_DIR + "/" + DB_NAME;

    /**
     * Inisialisasi database: membuat folder data dan tabel jika belum ada.
     */
    public static void initializeDatabase() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Aktifkan Foreign Keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Tabel User
            stmt.execute("CREATE TABLE IF NOT EXISTS User (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "full_name TEXT NOT NULL," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ");");

            // Tabel Photo
            stmt.execute("CREATE TABLE IF NOT EXISTS Photo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "filePath TEXT NOT NULL" +
                    ");");

            // Tabel JournalEntry
            stmt.execute("CREATE TABLE IF NOT EXISTS JournalEntry (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "category TEXT," +
                    "title TEXT," +
                    "description TEXT," +
                    "trigger TEXT," +
                    "target TEXT," +
                    "date TEXT NOT NULL," +
                    "time TEXT NOT NULL," +
                    "photo_id INTEGER," +
                    "user_id INTEGER NOT NULL," +
                    "FOREIGN KEY (photo_id) REFERENCES Photo(id) ON DELETE SET NULL," +
                    "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE" +
                    ");");

            // Tabel SelfCareGoal
            stmt.execute("CREATE TABLE IF NOT EXISTS SelfCareGoal (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "is_completed INTEGER DEFAULT 0," +
                    "date TEXT NOT NULL" +
                    ");");

            // Index untuk mempercepat pencarian berdasarkan tanggal atau user
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_journal_date ON JournalEntry(date);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_journal_user ON JournalEntry(user_id);");

            System.out.println("Database initialized successfully with constraints and indexes.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Mendapatkan koneksi ke database SQLite.
     * Secara otomatis mengaktifkan dukungan Foreign Key.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void clearAllData() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM JournalEntry;");
            stmt.execute("DELETE FROM Photo;");
            stmt.execute("DELETE FROM SelfCareGoal;");
            stmt.execute("DELETE FROM User;");
            System.out.println("Semua data berhasil dikosongkan.");
        } catch (SQLException e) {
            System.err.println("Gagal mengosongkan data: " + e.getMessage());
        }
    }

    public static void dropAllTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS JournalEntry;");
            stmt.execute("DROP TABLE IF EXISTS Photo;");
            stmt.execute("DROP TABLE IF EXISTS SelfCareGoal;");
            stmt.execute("DROP TABLE IF EXISTS User;");
            System.out.println("Semua tabel berhasil dihapus.");
        } catch (SQLException e) {
            System.err.println("Gagal menghapus tabel: " + e.getMessage());
        }
    }
}
