package com.juki.view;

import com.juki.controller.EntryController;
import com.juki.controller.GoalController;
import com.juki.model.JournalEntry;
import com.juki.model.SelfCareGoal;
import com.juki.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.juki.utils.DesignSystem;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DashboardView {

    private User currentUser;
    private BorderPane mainRoot;

    public ScrollPane getDashboardView(User user, BorderPane root) {
        this.currentUser = user;
        this.mainRoot = root;
        EntryController entryController = new EntryController();
        List<JournalEntry> entries = entryController.getAllEntries(user.getId());
        
        GoalController goalController = new GoalController();
        List<SelfCareGoal> todayGoals = goalController.getGoalsByDate(LocalDate.now());

        VBox content = new VBox(48);
        content.setPadding(new Insets(52, 100, 52, 100));
        content.setStyle("-fx-background-color: white;");

        // Row 1: Greeting
        HBox greetingBox = new HBox(5);
        greetingBox.setAlignment(Pos.CENTER_LEFT);
        Label greetingText = new Label("Halo, " + user.getFullName() + "!  Gimana perasaanmu hari ini? ");
        greetingText.setTextFill(DesignSystem.getLemon(900));
        greetingText.setFont(Font.font("Outfit", FontWeight.MEDIUM, 50));
        
        ImageView emojiImage = new ImageView(new Image("file:img/emojis/thinking-face.png"));
        emojiImage.setFitWidth(50);
        emojiImage.setFitHeight(50);
        emojiImage.setPreserveRatio(true);

        greetingBox.getChildren().addAll(greetingText, emojiImage);
        // Row 2: Streak, Graph, Calendar, Mood Selector
        HBox row2 = new HBox(40);
        row2.setAlignment(Pos.BOTTOM_LEFT);

        // Column 1: Streak + Mood Graph
        VBox col1 = new VBox(10);
        col1.setPrefWidth(689);
        col1.getChildren().addAll(createStreakWidget(entries), createMoodGraphWidget(entries));

        // Column 2: Calendar
        VBox col2 = createCalendarWidget();

        // Column 3: Mood Selector
        VBox col3 = createMoodSelectorWidget();

        row2.getChildren().addAll(col1, col2, col3);

        // Row 3: Journal History + Daily Targets
        HBox row3 = new HBox(64);
        row3.setAlignment(Pos.TOP_LEFT);

        VBox journalHistory = createJournalHistoryWidget(entries);
        VBox dailyTargets = createDailyTargetsWidget(todayGoals);

        row3.getChildren().addAll(journalHistory, dailyTargets);

        content.getChildren().addAll(greetingBox, row2, row3);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: white;");
        
        return scrollPane;
    }

    private HBox createStreakWidget(List<JournalEntry> entries) {
        HBox container = new HBox(56);
        container.setPrefSize(689, 137);
        container.setPadding(new Insets(32));
        container.setStyle("-fx-background-color: white; -fx-border-color: #D6D6D6; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        container.setAlignment(Pos.CENTER);

        // Simple streak calculation
        int streakCountVal = (int) entries.stream().map(JournalEntry::getDate).distinct().count();

        // Streak Day Count
        VBox streakCount = new VBox(0);
        streakCount.setPrefWidth(95);
        streakCount.setAlignment(Pos.CENTER);
        
        AnchorPane streakIcon = new AnchorPane();
        streakIcon.setPrefSize(147.69, 75.56);
        
        ImageView fireImage = new ImageView(new Image("file:img/beranda/streak_fire.png"));
        fireImage.setFitWidth(42.15);
        fireImage.setFitHeight(60.45);
        fireImage.setPreserveRatio(true);
        // Absolute positioning for fire icon from HTML: left: 74.27px; top: 7.56px
        AnchorPane.setLeftAnchor(fireImage, 74.27);
        AnchorPane.setTopAnchor(fireImage, 7.56);
        
        Label dayLabel = new Label(String.valueOf(streakCountVal));
        dayLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 50));
        dayLabel.setTextFill(Color.web(DesignSystem.NEUTRAL_900));
        // Absolute positioning from HTML: left: 33px; top: 12.56px
        AnchorPane.setLeftAnchor(dayLabel, 33.0);
        AnchorPane.setTopAnchor(dayLabel, 12.56);
        
        streakIcon.getChildren().addAll(fireImage, dayLabel);
        
        Label streakText = new Label("day streak");
        streakText.setFont(Font.font("Outfit", FontWeight.MEDIUM, 20));
        streakText.setTextFill(Color.web(DesignSystem.NEUTRAL_800));
        streakCount.getChildren().addAll(streakIcon, streakText);

        // Target Self-care
        VBox targetSelfCare = new VBox(16);
        targetSelfCare.setAlignment(Pos.CENTER);
        Label targetTitle = new Label("Target Self-care");
        targetTitle.setFont(Font.font("Outfit", FontWeight.MEDIUM, 25));
        targetTitle.setTextFill(Color.web(DesignSystem.NEUTRAL_900));

        HBox daysRow = new HBox(24.11);
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};
        // Use real activity for the last 7 days
        LocalDate today = LocalDate.now();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            boolean hasEntry = entries.stream().anyMatch(e -> e.getDate().equals(d));
            
            VBox dayCol = new VBox(8.04);
            dayCol.setAlignment(Pos.CENTER);
            Circle dot = new Circle(12.05); // 24.11 / 2
            if (hasEntry) {
                dot.setFill(Color.web(DesignSystem.SUCCESS_GREEN));
            } else {
                dot.setFill(Color.TRANSPARENT);
                dot.setStroke(Color.web(DesignSystem.SUCCESS_GREEN));
                dot.setStrokeWidth(1.61);
            }
            Label dayChar = new Label(days[d.getDayOfWeek().getValue() % 7]);
            dayChar.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 16.07));
            dayChar.setTextFill(Color.web(DesignSystem.NEUTRAL_800));
            dayCol.getChildren().addAll(dot, dayChar);
            daysRow.getChildren().add(dayCol);
        }
        
        targetSelfCare.getChildren().addAll(targetTitle, daysRow);

        container.getChildren().addAll(streakCount, targetSelfCare);
        return container;
    }

    private VBox createMoodGraphWidget(List<JournalEntry> entries) {
        VBox container = new VBox(8);
        container.setPadding(new Insets(28));
        container.setStyle("-fx-background-color: white; -fx-border-color: " + DesignSystem.NEUTRAL_300 + "; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Grafik Suasana Hati");
        title.setFont(Font.font("Outfit", FontWeight.MEDIUM, 25));
        title.setTextFill(Color.web(DesignSystem.NEUTRAL_900));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox dateFilter = new HBox(10);
        dateFilter.setPadding(new Insets(8, 16, 8, 16));
        dateFilter.setStyle("-fx-background-color: " + DesignSystem.LEMON_100 + "; -fx-border-color: " + DesignSystem.BORDER_YELLOW + "; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        dateFilter.setAlignment(Pos.CENTER);
        
        ImageView calendarIcon = new ImageView(new Image("file:img/icons/calendar.png"));
        calendarIcon.setFitWidth(24);
        calendarIcon.setFitHeight(24);
        calendarIcon.setPreserveRatio(true);
        
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMM", new Locale("id", "ID"));
        Label dateRange = new Label(start.format(dtf) + " - " + end.format(dtf) + " " + end.getYear());
        dateRange.setFont(Font.font("Outfit", FontWeight.LIGHT, 15));
        dateRange.setTextFill(Color.web(DesignSystem.NEUTRAL_900));
        dateFilter.getChildren().addAll(calendarIcon, dateRange);
        
        header.getChildren().addAll(title, spacer, dateFilter);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 5, 1);
        yAxis.setTickLabelsVisible(false);
        yAxis.setTickMarkVisible(false);
        yAxis.setMinorTickVisible(false);
        
        AreaChart<String, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setPrefHeight(228);
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.getXAxis().setTickLabelsVisible(false);
        chart.getXAxis().setOpacity(0);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String[] dayShorts = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (int i = 6; i >= 0; i--) {
            LocalDate d = end.minusDays(i);
            int moodValue = entries.stream()
                .filter(e -> e.getDate().equals(d))
                .mapToInt(e -> {
                    String cat = e.getCategory();
                    if (cat == null) return 3;
                    if (cat.contains("Excited") || cat.contains("Senang")) return 5;
                    if (cat.contains("Sedih")) return 2;
                    if (cat.contains("Marah")) return 1;
                    return 3;
                }).findFirst().orElse(0);
            
            series.getData().add(new XYChart.Data<>(dayShorts[d.getDayOfWeek().getValue() % 7], moodValue));
        }
        chart.getData().add(series);

        HBox daysRow = new HBox(32);
        daysRow.setAlignment(Pos.CENTER);
        for (int i = 6; i >= 0; i--) {
            LocalDate d = end.minusDays(i);
            Label dayLabel = new Label(dayShorts[d.getDayOfWeek().getValue() % 7]);
            dayLabel.setFont(Font.font("Montserrat", FontWeight.MEDIUM, 16));
            dayLabel.setTextFill(Color.web(DesignSystem.NEUTRAL_500));
            daysRow.getChildren().add(dayLabel);
        }

        container.getChildren().addAll(header, chart, daysRow);
        return container;
    }

    private LocalDate viewedMonthDate = LocalDate.now().withDayOfMonth(1);

    private VBox createCalendarWidget() {
        VBox container = new VBox(40);
        container.setPrefSize(500, 500);
        container.setPadding(new Insets(28));
        container.setStyle("-fx-background-color: white; -fx-border-color: " + DesignSystem.NEUTRAL_300 + "; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        container.setAlignment(Pos.TOP_CENTER);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        
        javafx.scene.effect.ColorAdjust arrowColor = new javafx.scene.effect.ColorAdjust();
        arrowColor.setBrightness(0.65); // Matching #A5A5A5

        Label monthLabel = new Label();
        monthLabel.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        monthLabel.setTextFill(Color.web(DesignSystem.NEUTRAL_900));

        GridPane grid = new GridPane();
        grid.setHgap(32);
        grid.setVgap(40);
        grid.setAlignment(Pos.CENTER);

        Runnable updateCalendar = () -> {
            monthLabel.setText(viewedMonthDate.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new Locale("id", "ID")) + " " + viewedMonthDate.getYear());
            
            grid.getChildren().clear();
            String[] headers = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
            for (int i = 0; i < 7; i++) {
                Label h = new Label(headers[i]);
                h.setFont(Font.font("Montserrat", FontWeight.MEDIUM, 16));
                h.setTextFill(Color.web(DesignSystem.NEUTRAL_500));
                grid.add(h, i, 0);
            }

            int dayOfWeek = viewedMonthDate.getDayOfWeek().getValue() % 7;
            int daysInMonth = viewedMonthDate.lengthOfMonth();

            int day = 1;
            LocalDate today = LocalDate.now();
            for (int row = 1; row <= 6; row++) {
                for (int col = 0; col < 7; col++) {
                    if (row == 1 && col < dayOfWeek) continue;
                    if (day <= daysInMonth) {
                        Label d = new Label(String.valueOf(day));
                        d.setFont(Font.font("Montserrat", FontWeight.NORMAL, 20));
                        
                        boolean isToday = today.getYear() == viewedMonthDate.getYear() && 
                                          today.getMonth() == viewedMonthDate.getMonth() && 
                                          today.getDayOfMonth() == day;
                        
                        d.setTextFill(isToday ? DesignSystem.getViolet(800) : Color.web(DesignSystem.NEUTRAL_800));
                        if (isToday) d.setFont(Font.font("Montserrat", FontWeight.BOLD, 20));
                        
                        d.setMinWidth(40);
                        d.setAlignment(Pos.CENTER);
                        grid.add(d, col, row);
                        day++;
                    }
                }
            }
        };

        ImageView btnPrev = new ImageView(new Image("file:img/icons/arrow-left.png"));
        btnPrev.setFitWidth(32);
        btnPrev.setPreserveRatio(true);
        btnPrev.setPickOnBounds(true);
        btnPrev.setStyle("-fx-cursor: hand;");
        btnPrev.setEffect(arrowColor);
        btnPrev.setOnMouseClicked(e -> {
            viewedMonthDate = viewedMonthDate.minusMonths(1).withDayOfMonth(1);
            System.out.println("Navigating to previous month: " + viewedMonthDate);
            updateCalendar.run();
        });
        
        Region s1 = new Region(); HBox.setHgrow(s1, Priority.ALWAYS);
        Region s2 = new Region(); HBox.setHgrow(s2, Priority.ALWAYS);
        
        ImageView btnNext = new ImageView(new Image("file:img/icons/arrow-right.png"));
        btnNext.setFitWidth(32);
        btnNext.setPreserveRatio(true);
        btnNext.setPickOnBounds(true);
        btnNext.setStyle("-fx-cursor: hand;");
        btnNext.setEffect(arrowColor);
        btnNext.setOnMouseClicked(e -> {
            viewedMonthDate = viewedMonthDate.plusMonths(1).withDayOfMonth(1);
            System.out.println("Navigating to next month: " + viewedMonthDate);
            updateCalendar.run();
        });
        
        header.getChildren().addAll(btnPrev, s1, monthLabel, s2, btnNext);

        updateCalendar.run();

        container.getChildren().addAll(header, grid);
        return container;
    }

    private VBox createMoodSelectorWidget() {
        String[] emotionNames = {"Angry", "Bored", "Confused", "Excited", "Guilty", "Hurt", "Hyperactive", "Insecure", "Joyful", "Sensitive", "Stressed", "Tired"};
        String[] emotionImages = {"angry.png", "bored.png", "confused.png", "excited.png", "guilty.png", "hurt.png", "hyperactive.png", "insecure.png", "joyful.png", "sensitive.png", "stressed.png", "tired.png"};
        final int[] currentIndex = {3}; // Default to "Excited" (index 3)

        VBox container = new VBox(32);
        container.setPadding(new Insets(28, 32, 28, 32));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 20px;");
        container.setPrefWidth(600);
        container.setAlignment(Pos.TOP_LEFT);

        VBox titleArea = new VBox(4);
        Label title = new Label("Moodmu Hari Ini");
        title.setFont(Font.font("Outfit", FontWeight.MEDIUM, 30));
        title.setTextFill(Color.web(DesignSystem.NEUTRAL_900));
        Label subtitle = new Label("Pilih emosi yang paling mewakilimu saat ini!");
        subtitle.setFont(Font.font("Outfit", FontWeight.LIGHT, 20));
        subtitle.setTextFill(Color.web(DesignSystem.NEUTRAL_800));
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(540); // Slightly less than card width to allow padding
        titleArea.getChildren().addAll(title, subtitle);

        VBox moodSelection = new VBox(8);
        moodSelection.setAlignment(Pos.CENTER);
        
        HBox selector = new HBox(20);
        selector.setPrefSize(387, 241.96);
        selector.setAlignment(Pos.CENTER);
        
        ImageView moodImg = new ImageView(new Image("file:img/emotions/" + emotionImages[currentIndex[0]]));
        moodImg.setFitWidth(150);
        moodImg.setPreserveRatio(true);

        Label moodName = new Label(emotionNames[currentIndex[0]]);
        moodName.setFont(Font.font("Outfit", FontWeight.MEDIUM, 30));
        moodName.setTextFill(Color.web(DesignSystem.NEUTRAL_900));

        // Effects for arrow states
        javafx.scene.effect.ColorAdjust activeAdjust = new javafx.scene.effect.ColorAdjust();
        activeAdjust.setBrightness(0.65); // #A5A5A5 approx

        javafx.scene.effect.ColorAdjust disabledAdjust = new javafx.scene.effect.ColorAdjust();
        disabledAdjust.setBrightness(0.9); // Very light grey #E6E6E6 approx

        ImageView btnLeft = new ImageView(new Image("file:img/icons/arrow-left.png"));
        btnLeft.setFitWidth(32);
        btnLeft.setPreserveRatio(true);
        btnLeft.setStyle("-fx-cursor: hand;");
        
        ImageView btnRight = new ImageView(new Image("file:img/icons/arrow-right.png"));
        btnRight.setFitWidth(32);
        btnRight.setPreserveRatio(true);
        btnRight.setStyle("-fx-cursor: hand;");

        // Helper to update arrow visual state
        Runnable updateArrows = () -> {
            btnLeft.setEffect(currentIndex[0] == 0 ? disabledAdjust : activeAdjust);
            btnLeft.setOpacity(currentIndex[0] == 0 ? 0.5 : 1.0);
            
            btnRight.setEffect(currentIndex[0] == emotionNames.length - 1 ? disabledAdjust : activeAdjust);
            btnRight.setOpacity(currentIndex[0] == emotionNames.length - 1 ? 0.5 : 1.0);
        };

        btnLeft.setOnMouseClicked(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                moodImg.setImage(new Image("file:img/emotions/" + emotionImages[currentIndex[0]]));
                moodName.setText(emotionNames[currentIndex[0]]);
                updateArrows.run();
            }
        });
        
        btnRight.setOnMouseClicked(e -> {
            if (currentIndex[0] < emotionNames.length - 1) {
                currentIndex[0]++;
                moodImg.setImage(new Image("file:img/emotions/" + emotionImages[currentIndex[0]]));
                moodName.setText(emotionNames[currentIndex[0]]);
                updateArrows.run();
            }
        });
        
        updateArrows.run(); // Initial state
        
        selector.getChildren().addAll(btnLeft, moodImg, btnRight);
        moodSelection.getChildren().addAll(selector, moodName);

        Button btnCatat = new Button("Catat");
        try {
            ImageView notesIcon = new ImageView(new Image("file:img/icons/notes.png"));
            notesIcon.setFitWidth(32);
            notesIcon.setFitHeight(32);
            notesIcon.setPreserveRatio(true);
            // Change color to #74400F
            javafx.scene.effect.ColorAdjust notesColor = new javafx.scene.effect.ColorAdjust();
            notesColor.setBrightness(-0.5); // Darken to match brown
            notesIcon.setEffect(notesColor);
            btnCatat.setGraphic(notesIcon);
            btnCatat.setGraphicTextGap(10);
        } catch (Exception e) {
            System.err.println("Could not load notes icon: " + e.getMessage());
        }
        btnCatat.setMaxWidth(Double.MAX_VALUE);
        btnCatat.setPrefHeight(52);
        // HTML: background: #FFE341; padding-left: 64px; padding-right: 64px; ... gap: 10px
        btnCatat.setStyle("-fx-background-color: " + DesignSystem.LEMON_300 + "; -fx-background-radius: 10px; -fx-text-fill: #74400F; -fx-font-family: 'Outfit'; -fx-font-size: 20px; -fx-padding: 16px 64px; -fx-cursor: hand;");
        
        btnCatat.setOnAction(e -> {
            if (mainRoot != null) {
                EntryFormView entryFormView = new EntryFormView(currentUser, () -> {
                    mainRoot.setCenter(getDashboardView(currentUser, mainRoot));
                });
                mainRoot.setCenter(entryFormView.getView().getCenter());
            }
        });
        
        container.getChildren().addAll(titleArea, moodSelection, btnCatat);
        return container;
    }

    private VBox createJournalHistoryWidget(List<JournalEntry> entries) {
        VBox container = new VBox(24);
        container.setPrefWidth(1158);

        Label title = new Label("Riwayat Jurnal");
        title.setFont(Font.font("Outfit", FontWeight.MEDIUM, 30));
        title.setTextFill(Color.web("#292929"));

        HBox cards = new HBox(18);
        if (entries.isEmpty()) {
            Label empty = new Label("Belum ada jurnal. Yuk mulai menulis!");
            empty.setFont(Font.font("Outfit", 20));
            cards.getChildren().add(empty);
        } else {
            for (int i = 0; i < Math.min(entries.size(), 2); i++) {
                JournalEntry entry = entries.get(i);
                cards.getChildren().add(createJournalCard(
                    entry.getDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("id", "ID"))), 
                    entry.getTitle(), 
                    entry.getDescription()
                ));
            }
        }

        container.getChildren().addAll(title, cards);
        return container;
    }

    private VBox createJournalCard(String date, String title, String content) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(28));
        card.setPrefWidth(480);
        card.setStyle("-fx-background-color: " + DesignSystem.LEMON_100 + "; -fx-background-radius: 20px;");

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        Label dateLabel = new Label(date);
        dateLabel.setFont(Font.font("Outfit", FontWeight.NORMAL, 25));
        dateLabel.setTextFill(Color.web(DesignSystem.NEUTRAL_900));
        
        Circle dot = new Circle(5, Color.web(DesignSystem.STREAK_ORANGE));
        header.getChildren().addAll(dateLabel, dot);

        VBox body = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 22));
        titleLabel.setTextFill(Color.web(DesignSystem.NEUTRAL_800));
        
        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setMaxHeight(100);
        contentLabel.setFont(Font.font("Outfit", FontWeight.LIGHT, 20));
        contentLabel.setTextFill(Color.BLACK);
        body.getChildren().addAll(titleLabel, contentLabel);

        card.getChildren().addAll(header, body);
        return card;
    }

    private VBox createDailyTargetsWidget(List<SelfCareGoal> goals) {
        VBox container = new VBox(16);
        container.setPrefSize(500, 302);
        container.setPadding(new Insets(28));
        container.setStyle("-fx-background-color: white; -fx-border-color: " + DesignSystem.NEUTRAL_300 + "; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        Circle iconPlaceholder = new Circle(35, Color.web("#D9D9D9"));
        VBox titleArea = new VBox(4);
        Label title = new Label("Target Hari Ini");
        title.setFont(Font.font("Outfit", FontWeight.MEDIUM, 30));
        title.setTextFill(Color.web(DesignSystem.NEUTRAL_900));
        Label subtitle = new Label("Peluk dirimu dengan kegiatan ini!");
        subtitle.setFont(Font.font("Outfit", FontWeight.LIGHT, 20));
        subtitle.setTextFill(Color.web(DesignSystem.NEUTRAL_800));
        titleArea.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(iconPlaceholder, titleArea);

        VBox list = new VBox(8);
        if (goals.isEmpty()) {
            list.getChildren().add(new Label("Belum ada target hari ini."));
        } else {
            for (SelfCareGoal goal : goals) {
                list.getChildren().add(createTargetItem(goal.getTitle(), goal.isCompleted()));
            }
        }

        container.getChildren().addAll(header, list);
        return container;
    }

    private HBox createTargetItem(String text, boolean completed) {
        HBox item = new HBox();
        item.setPrefHeight(40);
        item.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(text);
        label.setFont(Font.font("Outfit", FontWeight.LIGHT, 20));
        label.setTextFill(Color.BLACK);
        if (completed) {
            label.setStyle("-fx-text-decoration: line-through;");
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        if (completed) {
            Circle dot = new Circle(17.5, Color.web(DesignSystem.SUCCESS_GREEN));
            item.getChildren().addAll(label, spacer, dot);
        } else {
            Circle dot = new Circle(17.5, Color.TRANSPARENT);
            dot.setStroke(Color.web(DesignSystem.SUCCESS_GREEN));
            dot.setStrokeWidth(2);
            item.getChildren().addAll(label, spacer, dot);
        }
        
        return item;
    }
}
