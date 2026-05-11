package com.juki.view;

import com.juki.controller.EntryController;
import com.juki.model.JournalEntry;
import com.juki.model.User;
import com.juki.utils.DesignSystem;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EntryFormView {
    private TextField titleField;
    private ComboBox<String> catCombo;
    private TextField causeField;
    private TextArea writeArea;
    private User user;
    private Runnable onPostSuccess;

    public EntryFormView(User user, Runnable onPostSuccess) {
        this.user = user;
        this.onPostSuccess = onPostSuccess;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        // ==========================================
        // 1. TOP NAVIGATION BAR (Header)
        // ==========================================
        HBox navBar = new HBox(32);
        navBar.setStyle("-fx-background-color: #A114AC; -fx-padding: 42px 100px;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        HBox logoBox = new HBox(32);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        Label logoLabel = new Label("JuK");
        logoLabel.setTextFill(Color.WHITE);
        logoLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 50));
        Rectangle logoBar = new Rectangle(7, 35, Color.WHITE);
        logoBox.getChildren().addAll(logoLabel, logoBar);

        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setStyle("-fx-background-color: white; -fx-background-radius: 100px; -fx-padding: 16px 32px; -fx-border-color: #D6D6D6; -fx-border-radius: 100px;");
        searchContainer.setPrefWidth(581);
        
        ImageView searchIcon = new ImageView(new Image("file:img/icons/more.png")); // Placeholder for search icon
        searchIcon.setFitWidth(32);
        searchIcon.setFitHeight(32);
        javafx.scene.effect.ColorAdjust searchIconColor = new javafx.scene.effect.ColorAdjust();
        searchIconColor.setBrightness(0.65); // #A5A5A5
        searchIcon.setEffect(searchIconColor);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Cari Jurnal");
        searchBar.setStyle("-fx-background-color: transparent; -fx-prompt-text-fill: rgba(0,0,0,0.20); -fx-font-family: 'Outfit'; -fx-font-size: 22px; -fx-font-weight: 300;");
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        searchContainer.getChildren().addAll(searchIcon, searchBar);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(64);
        navLinks.setAlignment(Pos.CENTER);
        
        Label navBeranda = new Label("Beranda");
        navBeranda.setTextFill(Color.web("#F2F6FC"));
        navBeranda.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        
        Label navJurnal = new Label("Jurnal");
        navJurnal.setTextFill(Color.web("#F2F6FC"));
        navJurnal.setFont(Font.font("Outfit", FontWeight.BOLD, 25)); // Penanda aktif
        
        Label navKalender = new Label("Kalender");
        navKalender.setTextFill(Color.web("#F2F6FC"));
        navKalender.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));

        Button btnTulis = new Button("Tulis Jurnal");
        btnTulis.setStyle("-fx-background-color: white; -fx-text-fill: #A114AC; -fx-font-family: 'Outfit'; -fx-font-size: 25px; -fx-background-radius: 10px; -fx-padding: 16px 32px; -fx-cursor: hand;");

        navLinks.getChildren().addAll(navBeranda, navJurnal, navKalender, btnTulis);
        navBar.getChildren().addAll(logoBox, searchContainer, spacer, navLinks);
        root.setTop(navBar);

        // ==========================================
        // 2. KONTEN UTAMA (Center)
        // ==========================================
        AnchorPane mainAnchor = new AnchorPane();
        mainAnchor.setStyle("-fx-background-color: white;");
        
        VBox content = new VBox(32);
        content.setPadding(new Insets(143 - 115, 363, 60, 363)); // Adjusted for ScrollPane relative to navbar
        content.setPrefWidth(1193 + 363 * 2); // Matching design width

        // Baris 1: Status & Aksi
        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER_LEFT);
        row1.setPrefWidth(1194);
        
        Label statusLbl = new Label("Draft");
        statusLbl.setTextFill(Color.web("#434343"));
        statusLbl.setFont(Font.font("Outfit", FontWeight.NORMAL, 40));

        Region spacerRow1 = new Region();
        HBox.setHgrow(spacerRow1, Priority.ALWAYS);

        Button btnPost = new Button("Post");
        btnPost.setStyle("-fx-background-color: #FFE341; -fx-text-fill: black; -fx-font-family: 'Outfit'; -fx-font-size: 30px; -fx-background-radius: 100px; -fx-padding: 8px 32px; -fx-cursor: hand;");
        btnPost.setOnAction(e -> handlePost());

        ImageView profilePic = new ImageView(new Image("https://ui-avatars.com/api/?name=" + user.getFullName() + "&background=8D1395&color=fff", true)); 
        profilePic.setFitWidth(60);
        profilePic.setFitHeight(60);
        Circle clip = new Circle(30, 30, 30);
        profilePic.setClip(clip);

        HBox actionBox = new HBox(16, btnPost, profilePic);
        actionBox.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(statusLbl, spacerRow1, actionBox);

        // Baris 2: Judul
        titleField = new TextField();
        titleField.setPromptText("Judul");
        titleField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-prompt-text-fill: rgba(0,0,0,0.20); -fx-padding: 0px;");
        titleField.setFont(Font.font("Outfit", FontWeight.MEDIUM, 75));

        // Baris 3: Kategori
        VBox row3 = new VBox(16);
        Label catLbl = new Label("Kategori");
        catLbl.setFont(Font.font("Outfit", FontWeight.NORMAL, 30));
        catCombo = new ComboBox<>();
        catCombo.setPromptText("Pilih Kategori");
        catCombo.getItems().addAll("Pekerjaan", "Keluarga", "Pendidikan", "Kesehatan");
        catCombo.setMaxWidth(Double.MAX_VALUE);
        catCombo.setStyle("-fx-background-color: white; -fx-border-color: rgba(0,0,0,0.20); -fx-border-radius: 100px; -fx-background-radius: 100px; -fx-padding: 16px 24px; -fx-font-family: 'Outfit'; -fx-font-size: 20px;");
        row3.getChildren().addAll(catLbl, catCombo);

        // Baris 4: Penyebab
        VBox row4 = new VBox(16);
        Label causeLbl = new Label("Penyebab");
        causeLbl.setFont(Font.font("Outfit", FontWeight.NORMAL, 30));
        causeField = new TextField();
        causeField.setPromptText("Tulis Penyebab");
        causeField.setStyle("-fx-background-color: white; -fx-border-color: rgba(0,0,0,0.20); -fx-border-radius: 100px; -fx-background-radius: 100px; -fx-padding: 16px 24px; -fx-font-family: 'Outfit'; -fx-font-size: 20px;");
        row4.getChildren().addAll(causeLbl, causeField);

        // Baris 5: Area Tulis Jurnal
        writeArea = new TextArea();
        writeArea.setPromptText("Tulis ceritamu hari ini!");
        writeArea.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; -fx-border-color: transparent; -fx-font-family: 'Outfit'; -fx-font-size: 30px; -fx-font-weight: 400; -fx-text-fill: #434343;");
        writeArea.setPrefHeight(150);
        writeArea.setWrapText(true);

        // Baris 6: Card Target Hari Ini
        VBox row6 = new VBox(16);
        row6.setPadding(new Insets(28));
        row6.setStyle("-fx-background-color: white; -fx-border-color: #D6D6D6; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        
        HBox targetHeader = new HBox(16);
        targetHeader.setAlignment(Pos.CENTER_LEFT);
        Circle targetCircle = new Circle(35, Color.web("#D9D9D9"));
        
        VBox targetTexts = new VBox(4);
        Label targetTitleLabel = new Label("Target Hari Ini");
        targetTitleLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 30));
        targetTitleLabel.setTextFill(Color.web("#292929"));
        Label targetDesc = new Label("Peluk dirimu dengan kegiatan ini!");
        targetDesc.setTextFill(Color.web("#434343"));
        targetDesc.setFont(Font.font("Outfit", FontWeight.LIGHT, 20));
        targetTexts.getChildren().addAll(targetTitleLabel, targetDesc);
        
        targetHeader.getChildren().addAll(targetCircle, targetTexts);

        VBox targetsList = new VBox(8);
        targetsList.getChildren().addAll(
            createTargetItem("Target 1", true),
            createTargetItem("Target 2", true),
            createTargetItem("Target 3", false),
            createTargetItem("Target 4", false)
        );

        row6.getChildren().addAll(targetHeader, targetsList);

        content.getChildren().addAll(row1, titleField, row3, row4, writeArea, row6);
        
        // Floating Camera Button
        Button btnCamera = new Button();
        btnCamera.setShape(new Circle(32.5));
        btnCamera.setMinSize(65, 65);
        btnCamera.setMaxSize(65, 65);
        btnCamera.setStyle("-fx-background-color: white; -fx-border-color: #D6D6D6; -fx-border-radius: 32.5; -fx-background-radius: 32.5; -fx-cursor: hand;");
        
        ImageView camIcon = new ImageView(new Image("file:img/icons/more.png")); // Placeholder
        camIcon.setFitWidth(32);
        camIcon.setFitHeight(32);
        btnCamera.setGraphic(camIcon);
        
        AnchorPane.setLeftAnchor(btnCamera, 271.0);
        AnchorPane.setTopAnchor(btnCamera, 631.0 - 115); // Offset by navbar

        mainAnchor.getChildren().addAll(content, btnCamera);

        ScrollPane scrollPane = new ScrollPane(mainAnchor);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");

        root.setCenter(scrollPane);
        
        return root;
    }

    private HBox createTargetItem(String text, boolean completed) {
        HBox item = new HBox();
        item.setPrefHeight(40);
        item.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(text);
        label.setFont(Font.font("Outfit", FontWeight.LIGHT, 25));
        label.setTextFill(Color.BLACK);
        if (completed) {
            label.setStyle("-fx-text-decoration: line-through;");
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        if (completed) {
            Circle dot = new Circle(17.5, Color.web("#82DD55"));
            item.getChildren().addAll(label, spacer, dot);
        } else {
            Circle dot = new Circle(17.5, Color.TRANSPARENT);
            dot.setStroke(Color.web("#82DD55"));
            dot.setStrokeWidth(2);
            item.getChildren().addAll(label, spacer, dot);
        }
        
        return item;
    }

    private void handlePost() {
        System.out.println("Tombol Post ditekan! Menyimpan jurnal ke database...");

        EntryController controller = new EntryController();
        JournalEntry entry = new JournalEntry();
        
        entry.setTitle(titleField.getText());
        entry.setCategory(catCombo.getValue() != null ? catCombo.getValue() : "Umum");
        entry.setTrigger(causeField.getText());
        entry.setDescription(writeArea.getText());
        entry.setDate(LocalDate.now());
        entry.setTime(LocalTime.now());
        entry.setUserId(user.getId());

        controller.addEntry(entry);

        if (onPostSuccess != null) {
            onPostSuccess.run();
        }
    }
}
