package com.juki.view;

import com.juki.controller.EntryController;
import com.juki.model.JournalEntry;
import com.juki.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DashboardView {

    public ScrollPane getDashboardView(User user) {
        EntryController entryController = new EntryController();
        List<JournalEntry> entries = entryController.getAllEntries(user.getId());

        VBox content = new VBox(25); // Jarak antara sapaan dan dashboard diperkecil
        content.setPadding(new Insets(30, 50, 30, 50)); // Ruang kosong di pinggir layar dikurangi
        
        Label greeting = new Label("Halo, " + user.getFullName() + "! Gimana perasaanmu hari ini? \uD83E\uDD14");
        greeting.setTextFill(Color.web("#74400F"));
        greeting.setFont(Font.font("Outfit", FontWeight.MEDIUM, 50));
        
        // --- PEMBUATAN WIDGET ---

        // Widget: Target Self-care (Streak)
        HBox streakWidget = new HBox(20);
        streakWidget.setStyle("-fx-background-color: white; -fx-background-radius: 15px; -fx-padding: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 0, 10, 0, 5);");
        streakWidget.setAlignment(Pos.CENTER_LEFT);
        Label streakIcon = new Label("🔥");
        streakIcon.setFont(Font.font(40));
        VBox streakText = new VBox();
        Label streakTitle = new Label("Target Self-care");
        streakTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
        Label streakDays = new Label("5 Hari Beruntun!");
        streakDays.setTextFill(Color.web("#8D1395"));
        streakDays.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        streakText.getChildren().addAll(streakTitle, streakDays);
        streakWidget.getChildren().addAll(streakIcon, streakText);

        // Widget: Grafik Suasana Hati
        VBox chartWidget = createWidgetContainer();
        Label chartLbl = new Label("Grafik Suasana Hati (Seminggu Terakhir)");
        chartLbl.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        
        if (entries.isEmpty()) {
            Label emptyChartLbl = new Label("Belum ada data suasana hati. Yuk, mulai isi jurnalmu!");
            emptyChartLbl.setFont(Font.font("Outfit", 16));
            emptyChartLbl.setTextFill(Color.GRAY);
            chartWidget.getChildren().addAll(chartLbl, emptyChartLbl);
        } else {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis(0, 5, 1);
            AreaChart<String, Number> chart = new AreaChart<>(xAxis, yAxis);
            chart.setPrefHeight(250);
            chart.setLegendVisible(false);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("Sen", 3));
            series.getData().add(new XYChart.Data<>("Sel", 4));
            series.getData().add(new XYChart.Data<>("Rab", 2));
            series.getData().add(new XYChart.Data<>("Kam", 5));
            series.getData().add(new XYChart.Data<>("Jum", 4));
            series.getData().add(new XYChart.Data<>("Sab", 4));
            series.getData().add(new XYChart.Data<>("Min", 5));
            chart.getData().add(series);
            chartWidget.getChildren().addAll(chartLbl, chart);
        }

        // Widget: Kalender Minimalis
        VBox calWidget = createWidgetContainer();
        Label calLbl = new Label("April 2024");
        calLbl.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        GridPane calGrid = new GridPane();
        calGrid.setHgap(15); calGrid.setVgap(15);
        String[] days = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for(int i=0; i<7; i++) {
            Label d = new Label(days[i]);
            d.setStyle("-fx-font-weight: bold; -fx-text-fill: #8D1395;");
            calGrid.add(d, i, 0);
        }
        int dayCounter = 1;
        for(int row=1; row<=5; row++) {
            for(int col=0; col<7; col++) {
                if(dayCounter <= 30) {
                    Label dayNum = new Label(String.valueOf(dayCounter));
                    dayNum.setFont(Font.font(14));
                    if (dayCounter == 10) { // Highlight today
                        dayNum.setStyle("-fx-background-color: #8D1395; -fx-text-fill: white; -fx-background-radius: 20px; -fx-padding: 5px 10px;");
                    } else {
                        dayNum.setStyle("-fx-padding: 5px 10px;");
                    }
                    calGrid.add(dayNum, col, row);
                    dayCounter++;
                }
            }
        }
        calWidget.getChildren().addAll(calLbl, calGrid);

        // Widget: Riwayat Jurnal
        VBox historyWidget = createWidgetContainer();
        Label historyLbl = new Label("Riwayat Jurnal");
        historyLbl.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        HBox historyCards = new HBox(15);
        
        if (entries.isEmpty()) {
            Label emptyHistLbl = new Label("Belum ada jurnal yang ditulis. Yuk, tulis jurnal pertamamu hari ini!");
            emptyHistLbl.setFont(Font.font("Outfit", 16));
            emptyHistLbl.setTextFill(Color.GRAY);
            historyCards.getChildren().add(emptyHistLbl);
        } else {
            int count = 0;
            for (JournalEntry entry : entries) {
                if (count >= 2) break; // Hanya tampilkan 2 jurnal terbaru di Dashboard
                String title = entry.getTitle() != null ? entry.getTitle() : "Tanpa Judul";
                String date = entry.getDate() != null ? entry.getDate().toString() : "Tanpa Tanggal";
                historyCards.getChildren().add(createJournalCard(title, date, "📝"));
                count++;
            }
        }
        historyWidget.getChildren().addAll(historyLbl, historyCards);

        // Widget: Input Mood
        VBox moodWidget = new VBox(20);
        moodWidget.setStyle("-fx-background-color: #BBDEFB; -fx-background-radius: 15px; -fx-padding: 30px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");
        moodWidget.setAlignment(Pos.CENTER);
        Label moodLbl = new Label("Moodmu Hari ini \uD83D\uDC7B");
        moodLbl.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        moodLbl.setTextFill(Color.web("#1565C0"));
        
        HBox carousel = new HBox(20);
        carousel.setAlignment(Pos.CENTER);
        Button btnLeft = new Button("<");
        btnLeft.setStyle("-fx-background-color: white; -fx-background-radius: 50%; -fx-font-weight: bold; -fx-text-fill: #1565C0; -fx-font-size: 18px;");
        VBox centerEmotion = new VBox(10);
        centerEmotion.setAlignment(Pos.CENTER);
        ImageView imgView = new ImageView();
        imgView.setFitWidth(120);
        Label imgPlaceholder = new Label("🖼️");
        imgPlaceholder.setFont(Font.font(60));
        Label emotionName = new Label("😆 Excited");
        emotionName.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        emotionName.setTextFill(Color.web("#1565C0"));
        centerEmotion.getChildren().addAll(imgView, imgPlaceholder, emotionName);
        Button btnRight = new Button(">");
        btnRight.setStyle("-fx-background-color: white; -fx-background-radius: 50%; -fx-font-weight: bold; -fx-text-fill: #1565C0; -fx-font-size: 18px;");
        carousel.getChildren().addAll(btnLeft, centerEmotion, btnRight);
        
        Button btnCatat = new Button("Catat");
        btnCatat.setPrefWidth(200);
        btnCatat.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #3E2723; -fx-font-weight: bold; -fx-background-radius: 10px; -fx-padding: 10px 30px; -fx-font-size: 16px;");
        VBox.setMargin(btnCatat, new Insets(15, 0, 0, 0));
        moodWidget.getChildren().addAll(moodLbl, carousel, btnCatat);

        // Widget: Target Hari Ini
        VBox targetWidget = createWidgetContainer();
        Label targetLbl = new Label("Target Hari Ini");
        targetLbl.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        CheckBox cb1 = new CheckBox("Minum air 2L");
        cb1.setFont(Font.font("Outfit", 16));
        CheckBox cb2 = new CheckBox("Tidur 8 Jam");
        cb2.setSelected(true);
        cb2.setFont(Font.font("Outfit", 16));
        CheckBox cb3 = new CheckBox("Membaca buku 15 menit");
        cb3.setFont(Font.font("Outfit", 16));
        targetWidget.getChildren().addAll(targetLbl, cb1, cb2, cb3);

        // Susun Layout
        VBox dashboard = new VBox(20); // Jarak antar baris dikurangi
        HBox topRowLayout = new HBox(20); // Jarak antar kolom dikurangi
        VBox leftCol = new VBox(20);
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.getChildren().addAll(streakWidget, chartWidget);
        VBox midCol = new VBox(20);
        midCol.getChildren().add(calWidget);
        VBox rightTopCol = new VBox(20);
        rightTopCol.setMinWidth(300); // Mengubah lebar kaku menjadi lebar dinamis minimum
        HBox.setHgrow(rightTopCol, Priority.SOMETIMES);
        rightTopCol.getChildren().add(moodWidget);
        topRowLayout.getChildren().addAll(leftCol, midCol, rightTopCol);
        
        HBox bottomRowLayout = new HBox(20);
        HBox.setHgrow(historyWidget, Priority.ALWAYS);
        VBox rightBottomCol = new VBox(20);
        rightBottomCol.setMinWidth(300); // Mengubah lebar kaku menjadi lebar dinamis minimum
        HBox.setHgrow(rightBottomCol, Priority.SOMETIMES);
        rightBottomCol.getChildren().add(targetWidget);
        bottomRowLayout.getChildren().addAll(historyWidget, rightBottomCol);
        
        dashboard.getChildren().addAll(topRowLayout, bottomRowLayout);
        content.getChildren().addAll(greeting, dashboard);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #FDF3FF;");
        
        return scrollPane;
    }

    private VBox createWidgetContainer() {
        VBox box = new VBox(10); // Jarak komponen di dalam widget sedikit lebih rapat
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15px; -fx-padding: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");
        return box;
    }

    private VBox createJournalCard(String title, String date, String emoji) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: #FFF9C4; -fx-background-radius: 10px; -fx-padding: 15px;");
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
        Label dateLbl = new Label(date + " " + emoji);
        dateLbl.setTextFill(Color.GRAY);
        dateLbl.setFont(Font.font("Outfit", 14));
        card.getChildren().addAll(titleLbl, dateLbl);
        return card;
    }
}