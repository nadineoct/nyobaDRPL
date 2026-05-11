package com.juki;

import com.juki.controller.RegistrationFormController;
import com.juki.db.DatabaseHelper;
import com.juki.model.User;
import com.juki.view.DashboardView;
import com.juki.view.EntryFormView;
import com.juki.view.EntryListView;
import com.juki.view.ProfileView;
import com.juki.model.UserSession;
import com.juki.view.RegistrationFormView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inisialisasi Tabel SQLite
        DatabaseHelper.initializeDatabase();

        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage primaryStage) {
        RegistrationFormView loginView = new RegistrationFormView(user -> {
            // Simpan state user yang aktif ke dalam Session
            UserSession.getInstance().setActiveUser(user);
            showMainDashboard(primaryStage, user);
        });

        Scene scene = new Scene(loginView.getView(), 1280, 720);
        primaryStage.setTitle("JuKi - Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void showMainDashboard(Stage primaryStage, User user) {
        BorderPane root = new BorderPane();
        
        // Top Navigation Bar
        HBox navBar = new HBox();
        navBar.setStyle("-fx-background-color: #8D1395; -fx-padding: 20px 100px;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("JuKi");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Outfit", FontWeight.BOLD, 50));
        
        HBox menuBox = new HBox(64);
        menuBox.setAlignment(Pos.CENTER);
        
        Label navBeranda = new Label("Beranda");
        navBeranda.setTextFill(Color.web("#FDF3FF"));
        navBeranda.setFont(Font.font("Outfit", FontWeight.BOLD, 25));
        navBeranda.setStyle("-fx-cursor: hand;");

        Label navJurnal = new Label("Jurnal");
        navJurnal.setTextFill(Color.web("#F2F6FC"));
        navJurnal.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        navJurnal.setStyle("-fx-cursor: hand;");

        Label navKalendar = new Label("Kalendar");
        navKalendar.setTextFill(Color.web("#F2F6FC"));
        navKalendar.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        
        Button btnTulis = new Button("✨ Tulis Jurnal");
        btnTulis.setStyle("-fx-background-color: white; -fx-text-fill: #A114AC; -fx-font-family: 'Outfit'; -fx-font-size: 25px; -fx-background-radius: 10px; -fx-padding: 10px 20px;");

        // Avatar Profile Circle dinamis di Navbar
        StackPane btnProfile = new StackPane();
        javafx.scene.shape.Circle navAvatar = new javafx.scene.shape.Circle(25, Color.web("#FDF3FF"));
        Label navAvatarInitial = new Label();
        navAvatarInitial.setTextFill(Color.web("#8D1395"));
        navAvatarInitial.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        btnProfile.getChildren().addAll(navAvatar, navAvatarInitial);
        btnProfile.setStyle("-fx-cursor: hand;");
        btnProfile.setOnMouseEntered(e -> btnProfile.setOpacity(0.8));
        btnProfile.setOnMouseExited(e -> btnProfile.setOpacity(1.0));
        
        Runnable updateNavAvatar = () -> {
            User u = UserSession.getInstance().getActiveUser();
            if (u != null) {
                boolean hasImage = false;
                if (u.getProfileImagePath() != null && !u.getProfileImagePath().isEmpty()) {
                    try {
                        java.io.File f = new java.io.File(u.getProfileImagePath());
                        if (f.exists()) {
                            navAvatar.setFill(new javafx.scene.paint.ImagePattern(new javafx.scene.image.Image(f.toURI().toString())));
                            navAvatarInitial.setVisible(false);
                            hasImage = true;
                        }
                    } catch (Exception ex) { }
                }
                if (!hasImage) {
                    navAvatar.setFill(Color.web("#FDF3FF"));
                    navAvatarInitial.setText(u.getFullName().substring(0, 1).toUpperCase());
                    navAvatarInitial.setVisible(true);
                }
            }
        };
        updateNavAvatar.run(); // Load avatar awal

        menuBox.getChildren().addAll(navBeranda, navJurnal, navKalendar, btnTulis, btnProfile);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(logo, spacer, menuBox);
        root.setTop(navBar);

        // Event Navigation Routing
        navBeranda.setOnMouseClicked(e -> {
            navBeranda.setFont(Font.font("Outfit", FontWeight.BOLD, 25));
            navJurnal.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            DashboardView dashboardView = new DashboardView();
            root.setCenter(dashboardView.getDashboardView(user));
        });

        navJurnal.setOnMouseClicked(e -> {
            navJurnal.setFont(Font.font("Outfit", FontWeight.BOLD, 25));
            navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            EntryListView entryListView = new EntryListView(user);
            root.setCenter(entryListView.getView());
        });
        
        btnTulis.setOnAction(e -> {
            navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            navJurnal.setFont(Font.font("Outfit", FontWeight.BOLD, 25)); // Set aktif di Jurnal
            
            EntryFormView entryFormView = new EntryFormView(user, () -> {
                // Aksi saat jurnal berhasil diposting (Kembali ke List)
                navJurnal.setFont(Font.font("Outfit", FontWeight.BOLD, 25));
                navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
                EntryListView entryListView = new EntryListView(user);
                root.setCenter(entryListView.getView());
            });
            root.setCenter(entryFormView.getView().getCenter()); // Mengambil kontennya saja tanpa duplikasi navbar
        });

        btnProfile.setOnMouseClicked(e -> {
            navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            navJurnal.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            
            ProfileView profileView = new ProfileView(user, () -> {
                // Hapus panel utama dan panggil halaman login ulang (Logout)
                showLoginScreen(primaryStage);
            }, updateNavAvatar);
            root.setCenter(profileView.getView());
        });

        // Panggil View Beranda (Dashboard)
        DashboardView dashboardView = new DashboardView();
        root.setCenter(dashboardView.getDashboardView(user));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("data:text/css,.chart-series-area-fill { -fx-fill: rgba(255, 105, 180, 0.4); } .chart-series-area-line { -fx-stroke: #FF69B4; -fx-stroke-width: 3px; }");
        primaryStage.setTitle("JuKi - App");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}