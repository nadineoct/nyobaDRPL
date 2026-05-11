package com.juki;

import com.juki.controller.RegistrationFormController;
import com.juki.db.DatabaseHelper;
import com.juki.model.User;
import com.juki.view.DashboardView;
import com.juki.view.EntryFormView;
import com.juki.view.EntryListView;
import com.juki.view.RegistrationFormView;
import com.juki.utils.DesignSystem;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
            showMainDashboard(primaryStage, user);
        });

        Scene scene = new Scene(loginView.getView(), 1600, 900);
        primaryStage.setTitle("JuKi - Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void showMainDashboard(Stage primaryStage, User user) {
        BorderPane root = new BorderPane();
        
        // Top Navigation Bar
        HBox navBar = new HBox();
        navBar.setStyle("-fx-background-color: " + DesignSystem.VIOLET_800 + "; -fx-padding: 42px 100px;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        ImageView logo = new ImageView();
        try {
            logo.setImage(new javafx.scene.image.Image("file:img/beranda/logo.png"));
            logo.setFitHeight(50);
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Could not load logo: " + e.getMessage());
        }
        
        Rectangle divider = new Rectangle(7, 35);
        divider.setFill(Color.web("#FAE7FF"));
        divider.setArcWidth(0.18);
        divider.setArcHeight(0.18);

        HBox logoBox = new HBox(32);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.getChildren().addAll(logo, divider);
        
        HBox menuBox = new HBox(64);
        menuBox.setAlignment(Pos.CENTER);
        
        Label navBeranda = new Label("Beranda");
        navBeranda.setTextFill(Color.web(DesignSystem.VIOLET_50));
        navBeranda.setFont(Font.font("Outfit", FontWeight.SEMI_BOLD, 25));
        navBeranda.setStyle("-fx-cursor: hand;");

        Label navJurnal = new Label("Jurnal");
        navJurnal.setTextFill(Color.web("#F2F6FC"));
        navJurnal.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        navJurnal.setStyle("-fx-cursor: hand;");

        Label navKalendar = new Label("Kalendar");
        navKalendar.setTextFill(Color.web("#F2F6FC"));
        navKalendar.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        navKalendar.setStyle("-fx-cursor: hand;");
        
        Button btnNavbarAction = new Button("Tulis Jurnal");
        try {
            ImageView notesIcon = new ImageView(new Image("file:img/icons/notes.png"));
            notesIcon.setFitWidth(32);
            notesIcon.setFitHeight(32);
            notesIcon.setPreserveRatio(true);
            // Color #A114AC
            javafx.scene.effect.ColorAdjust notesColor = new javafx.scene.effect.ColorAdjust();
            notesColor.setHue(0.85); 
            notesColor.setSaturation(0.6);
            notesIcon.setEffect(notesColor);
            btnNavbarAction.setGraphic(notesIcon);
            btnNavbarAction.setGraphicTextGap(10);
        } catch (Exception e) {
            System.err.println("Could not load notes icon: " + e.getMessage());
        }
        btnNavbarAction.setStyle("-fx-background-color: white; -fx-text-fill: #A114AC; -fx-font-family: 'Outfit'; -fx-font-size: 25px; -fx-background-radius: 10px; -fx-padding: 16px 32px; -fx-cursor: hand;");
        
        menuBox.getChildren().addAll(navBeranda, navJurnal, navKalendar, btnNavbarAction);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(logoBox, spacer, menuBox);
        root.setTop(navBar);

        // Event Navigation Routing
        navBeranda.setOnMouseClicked(e -> {
            navBeranda.setFont(Font.font("Outfit", FontWeight.SEMI_BOLD, 25));
            navJurnal.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            navKalendar.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            btnNavbarAction.setText("Tulis Jurnal");
            DashboardView dashboardView = new DashboardView();
            root.setCenter(dashboardView.getDashboardView(user, root));
        });

        navJurnal.setOnMouseClicked(e -> {
            navJurnal.setFont(Font.font("Outfit", FontWeight.SEMI_BOLD, 25));
            navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            navKalendar.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
            btnNavbarAction.setText("Tulis Jurnal");
            EntryListView entryListView = new EntryListView(user);
            root.setCenter(entryListView.getView());
        });

        navKalendar.setOnMouseClicked(e -> {
            // Calendar is currently disabled
            System.out.println("Fitur Kalendar sedang dalam pengembangan!");
        });
        
        btnNavbarAction.setOnAction(e -> {
            if (btnNavbarAction.getText().equals("Tulis Jurnal")) {
                navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
                navJurnal.setFont(Font.font("Outfit", FontWeight.SEMI_BOLD, 25));
                navKalendar.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
                
                EntryFormView entryFormView = new EntryFormView(user, () -> {
                    navJurnal.setFont(Font.font("Outfit", FontWeight.SEMI_BOLD, 25));
                    navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
                    EntryListView entryListView = new EntryListView(user);
                    root.setCenter(entryListView.getView());
                });
                root.setCenter(entryFormView.getView().getCenter());
            } else {
                System.out.println("Aksi dipicu!");
            }
        });

        // Panggil View Beranda (Dashboard)
        DashboardView dashboardView = new DashboardView();
        root.setCenter(dashboardView.getDashboardView(user, root));

        Scene scene = new Scene(root, 1600, 900);
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