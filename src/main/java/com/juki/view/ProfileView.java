package com.juki.view;

import com.juki.controller.ProfileController;
import com.juki.model.User;
import com.juki.model.UserSession;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import java.io.File;

public class ProfileView {
    private VBox view;
    private ProfileController controller;
    private Runnable onLogout;
    private Runnable onAvatarUpdated;
    private User fallbackUser;

    public ProfileView(User user, Runnable onLogout, Runnable onAvatarUpdated) {
        this.fallbackUser = user;
        this.onLogout = onLogout;
        this.onAvatarUpdated = onAvatarUpdated;
        this.controller = new ProfileController();
        
        buildUI();
    }

    private void buildUI() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #FDF3FF;");

        // Mengambil profil data secara dinamis
        User profileData = controller.getActiveUser();
        if (profileData == null) {
            profileData = this.fallbackUser;
        }

        // Desain Avatar Circle
        Circle avatar = new Circle(70, Color.web("#8D1395"));
        Label avatarText = new Label(profileData.getFullName().substring(0, 1).toUpperCase());
        avatarText.setTextFill(Color.WHITE);
        avatarText.setFont(Font.font("Outfit", FontWeight.BOLD, 55));
        
        javafx.scene.layout.StackPane avatarPane = new javafx.scene.layout.StackPane();
        avatarPane.getChildren().addAll(avatar, avatarText);
        
        updateAvatarUI(profileData, avatar, avatarText);

        // Tombol Ubah Foto Profile
        Button changePhotoBtn = new Button("Ubah Foto Profile");
        changePhotoBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #8D1395; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-underline: true;");
        
        User finalProfileData = profileData;
        changePhotoBtn.setOnAction(e -> {
            handlePhotoUpload(finalProfileData, avatar, avatarText);
        });

        // Data User
        Label nameLabel = new Label(profileData.getFullName());
        nameLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 40));
        nameLabel.setTextFill(Color.web("#74400F"));

        Label usernameLabel = new Label("@" + profileData.getUsername());
        usernameLabel.setFont(Font.font("Outfit", FontWeight.NORMAL, 22));
        usernameLabel.setTextFill(Color.GRAY);

        // Tombol Log Out dengan efek animasi Hover 
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 12px 50px; -fx-background-radius: 10px; -fx-cursor: hand;");
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 12px 50px; -fx-background-radius: 10px; -fx-cursor: hand;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 12px 50px; -fx-background-radius: 10px; -fx-cursor: hand;"));
        
        logoutBtn.setOnAction(e -> controller.logout(onLogout));

        view.getChildren().addAll(avatarPane, changePhotoBtn, nameLabel, usernameLabel, logoutBtn);
    }

    private void handlePhotoUpload(User user, Circle avatar, Label avatarText) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String newPath = selectedFile.getAbsolutePath(); // Langsung ambil path asli
            
            if (newPath != null) {
                controller.updateProfilePhoto(user.getId(), newPath);
                user.setProfileImagePath(newPath);
                UserSession.getInstance().setActiveUser(user); // Simpan state terbaru
                updateAvatarUI(user, avatar, avatarText);
                if (onAvatarUpdated != null) onAvatarUpdated.run(); // Notifikasi Navbar agar ikut update
            }
        }
    }

    private void updateAvatarUI(User profileData, Circle avatar, Label avatarText) {
        if (profileData.getProfileImagePath() != null && !profileData.getProfileImagePath().isEmpty()) {
            File f = new File(profileData.getProfileImagePath());
            if (f.exists()) {
                avatar.setFill(new ImagePattern(new javafx.scene.image.Image(f.toURI().toString())));
                avatarText.setVisible(false);
                return;
            }
        }
        // Fallback default avatar jika gambar belum ada atau file tidak ditemukan
        avatar.setFill(Color.web("#8D1395"));
        avatarText.setText(profileData.getFullName().substring(0, 1).toUpperCase());
        avatarText.setVisible(true);
    }

    public VBox getView() { return view; }
}