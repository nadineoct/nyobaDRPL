package com.juki.view;

import com.juki.model.User;
import com.juki.utils.DesignSystem;
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

import java.time.LocalDate;
import java.util.Locale;

public class CalendarView {
    private User currentUser;
    private LocalDate viewedMonthDate = LocalDate.now().withDayOfMonth(1);
    private GridPane calendarGrid;
    private Label monthLabel;

    public CalendarView(User user) {
        this.currentUser = user;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // 1. Sidebar (Left)
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        // 2. Main Calendar Content (Center)
        VBox mainContent = createMainCalendar();
        root.setCenter(mainContent);

        return root;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(16);
        sidebar.setPrefWidth(294);
        sidebar.setPadding(new Insets(16));
        sidebar.setStyle("-fx-background-color: #FAE7FF;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox contentWrapper = new VBox();
        contentWrapper.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(contentWrapper, Priority.ALWAYS);

        // Streak Widget
        VBox streakWidget = new VBox();
        streakWidget.setPrefHeight(180);
        streakWidget.setPadding(new Insets(16));
        streakWidget.setStyle("-fx-background-color: white; -fx-background-radius: 20px;");
        streakWidget.setAlignment(Pos.CENTER);

        VBox streakContent = new VBox();
        streakContent.setAlignment(Pos.CENTER);
        
        AnchorPane streakIcon = new AnchorPane();
        streakIcon.setPrefSize(147.69, 75.56);
        Label streakNum = new Label("2");
        streakNum.setFont(Font.font("Outfit", FontWeight.MEDIUM, 50));
        streakNum.setTextFill(Color.BLACK);
        AnchorPane.setLeftAnchor(streakNum, 33.0);
        AnchorPane.setTopAnchor(streakNum, 12.56);

        ImageView fireImg = new ImageView(new Image("file:img/beranda/streak_fire.png"));
        fireImg.setFitWidth(42.15);
        fireImg.setFitHeight(60.45);
        AnchorPane.setLeftAnchor(fireImg, 74.27);
        AnchorPane.setTopAnchor(fireImg, 7.56);
        streakIcon.getChildren().addAll(streakNum, fireImg);

        Label streakText = new Label("day streak");
        streakText.setFont(Font.font("Outfit", FontWeight.MEDIUM, 20));
        streakText.setTextFill(Color.BLACK);

        streakWidget.getChildren().addAll(streakIcon, streakText);

        // Target Self-care Section
        VBox targetSection = new VBox(24);
        targetSection.setPadding(new Insets(24, 0, 24, 0));
        Label targetTitle = new Label("Target Self-care");
        targetTitle.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        targetTitle.setTextFill(Color.web("#292929"));
        targetTitle.setMaxWidth(Double.MAX_VALUE);
        targetTitle.setAlignment(Pos.CENTER);

        VBox targetList = new VBox(16);
        targetList.getChildren().addAll(
            createSidebarTargetItem("Target 1", true),
            createSidebarTargetItem("Target 2", true),
            createSidebarTargetItem("Target 3", true)
        );

        targetSection.getChildren().addAll(targetTitle, targetList);

        contentWrapper.getChildren().addAll(streakWidget, targetSection);

        // Profile Section (Bottom)
        HBox profileBox = new HBox(16);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        profileBox.setPadding(new Insets(20, 0, 20, 0));

        // Real profile icon using UI-Avatars or local placeholder
        ImageView profileImg = new ImageView();
        try {
            // Using a nice placeholder service for "real" look
            profileImg.setImage(new Image("https://ui-avatars.com/api/?name=" + currentUser.getFullName() + "&background=8D1395&color=fff", true));
        } catch (Exception e) {
            profileImg.setImage(new Image("file:img/icons/more.png"));
        }
        profileImg.setFitWidth(70);
        profileImg.setFitHeight(70);
        Circle clip = new Circle(35, 35, 35);
        profileImg.setClip(clip);

        Label profileName = new Label(currentUser.getFullName());
        profileName.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        profileName.setTextFill(Color.BLACK);

        profileBox.getChildren().addAll(profileImg, profileName);

        sidebar.getChildren().addAll(contentWrapper, profileBox);
        return sidebar;
    }

    private HBox createSidebarTargetItem(String text, boolean completed) {
        HBox item = new HBox(16);
        item.setAlignment(Pos.CENTER_LEFT);
        
        StackPane check = new StackPane();
        Circle circle = new Circle(17.5, Color.web("#82DD55"));
        Label checkMark = new Label("✓"); // Simple checkmark
        checkMark.setTextFill(Color.WHITE);
        checkMark.setFont(Font.font("Outfit", FontWeight.BOLD, 15));
        check.getChildren().addAll(circle, checkMark);

        Label label = new Label(text);
        label.setFont(Font.font("Outfit", FontWeight.NORMAL, 20));
        label.setTextFill(Color.web("#434343"));
        if (completed) {
            label.setStyle("-fx-text-decoration: line-through;");
        }

        item.getChildren().addAll(check, label);
        return item;
    }

    private VBox createMainCalendar() {
        VBox main = new VBox();
        main.setPrefWidth(1626);

        // Calendar Header
        HBox header = new HBox();
        header.setPrefHeight(120);
        header.setPadding(new Insets(38, 521, 38, 531));
        header.setStyle("-fx-background-color: white; -fx-border-color: #D6D6D6; -fx-border-width: 0 0 1 0;");
        header.setAlignment(Pos.CENTER);

        HBox nav = new HBox();
        nav.setPrefWidth(574);
        nav.setAlignment(Pos.CENTER);
        nav.setSpacing(100); // Approximate spacing to fill width

        ImageView btnPrev = new ImageView(new Image("file:img/icons/arrow-left.png"));
        btnPrev.setFitWidth(32);
        btnPrev.setPreserveRatio(true);
        btnPrev.setPickOnBounds(true);
        btnPrev.setCursor(javafx.scene.Cursor.HAND);
        btnPrev.setOnMouseClicked(e -> {
            viewedMonthDate = viewedMonthDate.minusMonths(1).withDayOfMonth(1);
            updateCalendar();
        });

        monthLabel = new Label();
        monthLabel.setFont(Font.font("Outfit", FontWeight.NORMAL, 35));
        monthLabel.setTextFill(Color.web("#292929"));

        ImageView btnNext = new ImageView(new Image("file:img/icons/arrow-right.png"));
        btnNext.setFitWidth(32);
        btnNext.setPreserveRatio(true);
        btnNext.setPickOnBounds(true);
        btnNext.setCursor(javafx.scene.Cursor.HAND);
        btnNext.setRotate(180); // Design shows rotated arrow for next
        btnNext.setOnMouseClicked(e -> {
            viewedMonthDate = viewedMonthDate.plusMonths(1).withDayOfMonth(1);
            updateCalendar();
        });

        Region s1 = new Region(); HBox.setHgrow(s1, Priority.ALWAYS);
        Region s2 = new Region(); HBox.setHgrow(s2, Priority.ALWAYS);

        nav.getChildren().addAll(btnPrev, s1, monthLabel, s2, btnNext);
        header.getChildren().add(nav);

        // Calendar Grid
        calendarGrid = new GridPane();
        VBox.setVgrow(calendarGrid, Priority.ALWAYS);
        calendarGrid.setStyle("-fx-background-color: white;");

        updateCalendar();

        main.getChildren().addAll(header, calendarGrid);
        return main;
    }

    private void updateCalendar() {
        monthLabel.setText(viewedMonthDate.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new Locale("id", "ID")));
        calendarGrid.getChildren().clear();

        // Clear constraints
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(col);
        }
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / 6);
            calendarGrid.getRowConstraints().add(row);
        }

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int startDayOffset = viewedMonthDate.getDayOfWeek().getValue() % 7;
        int daysInMonth = viewedMonthDate.lengthOfMonth();
        
        LocalDate prevMonth = viewedMonthDate.minusMonths(1);
        int daysInPrevMonth = prevMonth.lengthOfMonth();

        int dayCount = 1;
        int nextMonthDay = 1;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                VBox cell = new VBox(4);
                cell.setAlignment(Pos.TOP_CENTER);
                cell.setPadding(new Insets(16));
                cell.setStyle("-fx-border-color: #D6D6D6; -fx-border-width: 0.5;");

                Label dayNameLabel = null;
                if (row == 0) {
                    dayNameLabel = new Label(days[col]);
                    dayNameLabel.setFont(Font.font("Outfit", FontWeight.LIGHT, 15));
                    dayNameLabel.setTextFill(Color.web("rgba(0, 0, 0, 0.20)"));
                }

                Label dayNumLabel = new Label();
                dayNumLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 20));

                if (row == 0 && col < startDayOffset) {
                    // Previous Month Days
                    dayNumLabel.setText(String.valueOf(daysInPrevMonth - startDayOffset + col + 1));
                    dayNumLabel.setTextFill(Color.web("rgba(0, 0, 0, 0.20)"));
                } else if (dayCount <= daysInMonth) {
                    // Current Month Days
                    dayNumLabel.setText(String.valueOf(dayCount));
                    dayNumLabel.setTextFill(Color.web("#292929"));
                    
                    // Special case for design example (day 29 highlight)
                    if (dayCount == 29) {
                        VBox highlight = new VBox(dayNumLabel);
                        highlight.setPadding(new Insets(5));
                        highlight.setStyle("-fx-background-color: #FFE341; -fx-background-radius: 100px;");
                        highlight.setAlignment(Pos.CENTER);
                        highlight.setMinWidth(40);
                        
                        VBox targetPreview = new VBox(4);
                        targetPreview.getChildren().addAll(
                            createSmallTarget("Target 1"),
                            createSmallTarget("Target 2"),
                            createSmallTarget("Target 3"),
                            new Label("2 lainnya") {{ setFont(Font.font("Outfit", FontWeight.MEDIUM, 15)); }}
                        );
                        cell.getChildren().addAll(highlight, targetPreview);
                    } else {
                        cell.getChildren().add(dayNumLabel);
                    }
                    
                    if (dayNameLabel != null) cell.getChildren().add(0, dayNameLabel);
                    dayCount++;
                } else {
                    // Next Month Days
                    dayNumLabel.setText(String.valueOf(nextMonthDay++));
                    dayNumLabel.setTextFill(Color.web("rgba(0, 0, 0, 0.20)"));
                }

                if (cell.getChildren().isEmpty()) {
                    cell.getChildren().add(dayNumLabel);
                    if (dayNameLabel != null) cell.getChildren().add(0, dayNameLabel);
                }
                
                calendarGrid.add(cell, col, row);
            }
        }
    }

    private HBox createSmallTarget(String text) {
        HBox h = new HBox(8);
        h.setPadding(new Insets(5, 8, 5, 8));
        h.setStyle("-fx-background-color: #FFFAC1; -fx-background-radius: 10px;");
        h.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(10, Color.web("#82DD55"));
        Label l = new Label(text);
        l.setFont(Font.font("Outfit", FontWeight.NORMAL, 15));
        l.setTextFill(Color.web("#434343"));
        h.getChildren().addAll(dot, l);
        return h;
    }
}
