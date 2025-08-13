package com.terracotta004.javafxpixelgrid;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;

import javafx.animation.Timeline;

import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
/*    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }*/

    private static final int GRID_SIZE = 25;     // 25x25 grid
    private static final int CELL_SIZE = 20;     // each "pixel" is 20x20 for visibility
    private static final double GAP = 1;         // gap between cells (purely visual)
    private Rectangle[][] cells = new Rectangle[GRID_SIZE][GRID_SIZE];

    // Animation state

    private Timeline anim1;

    private Timeline anim2;

    private double phase = 0.0;  // for the sine wave

    private int tick = 0;        // for the moving checkerboard



    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();

        grid.setHgap(GAP);
        grid.setVgap(GAP);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.WHITE);  // default fill
                cell.setStroke(Color.LIGHTGRAY); // cell border

                // default: editable toggle

                cell.setOnMouseClicked(e -> toggleCell(cell));

                cells[row][col] = cell;

                // Optional: click to toggle color
                cell.setOnMouseClicked(e -> {
                    if (cell.getFill().equals(Color.WHITE)) {
                        cell.setFill(Color.BLACK);
                    } else {
                        cell.setFill(Color.WHITE);
                    }
                });

                grid.add(cell, col, row);
            }
        }


        // Create buttons

        Button btnEditable = new Button("Editable Grid");

        Button btnAnim1 = new Button("Animation 1");

        Button btnAnim2 = new Button("Animation 2");



        // Button actions

        btnEditable.setOnAction(e -> makeEditableGrid());

        btnAnim1.setOnAction(e -> startAnimation1());

        btnAnim2.setOnAction(e -> startAnimation2());



        // Layout for buttons

        HBox buttonBox = new HBox(10, btnEditable, btnAnim1, btnAnim2);

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-padding: 10;");



        // Main layout

        BorderPane root = new BorderPane();

        root.setCenter(grid);

        root.setBottom(buttonBox);

        /*Scene scene = new Scene(grid);
        primaryStage.setTitle("25x25 Pixel Grid");*/
        Scene scene = new Scene(root);
        primaryStage.setTitle("25x25 Pixel Grid — Animated");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Example: make all cells editable (toggle white/black on click)

    private void makeEditableGrid() {

        stopAnimations();

        for (Rectangle[] row : cells) {

            for (Rectangle cell : row) {

                cell.setOnMouseClicked(e -> {

                    if (cell.getFill().equals(Color.WHITE)) {

                        cell.setFill(Color.BLACK);

                    } else {

                        cell.setFill(Color.WHITE);

                    }

                });

            }

        }

    }



    private void toggleCell(Rectangle cell) {
        cell.setFill(cell.getFill().equals(Color.WHITE) ? Color.BLACK : Color.WHITE);
    }

    // --- Animation 1: flowing sine wave ---
    private void startAnimation1() {
        stopAnimations();
        // Disable clicking during animation (optional)
        clearCellHandlers();

        phase = 0.0;
        anim1 = new Timeline(new KeyFrame(Duration.millis(80), e -> {
            // wave constants
            double kRow = 0.45;
            double kCol = 0.45;

            for (int r = 0; r < GRID_SIZE; r++) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    double v = Math.sin(kRow * r + kCol * c + phase);
                    cells[r][c].setFill(v > 0 ? Color.BLACK : Color.WHITE);
                }
            }
            phase += 0.35; // advance wave
        }));
        anim1.setCycleCount(Timeline.INDEFINITE);
        anim1.play();
    }

    // --- Animation 2: moving checkerboard ---
    private void startAnimation2() {
        stopAnimations();
        clearCellHandlers();

        tick = 0;
        anim2 = new Timeline(new KeyFrame(Duration.millis(120), e -> {
            for (int r = 0; r < GRID_SIZE; r++) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    // shift pattern by "tick" so it scrolls
                    boolean isBlack = ((r + c + tick) & 1) == 0;
                    cells[r][c].setFill(isBlack ? Color.BLACK : Color.WHITE);
                }
            }
            tick++; // move one step each frame
        }));
        anim2.setCycleCount(Timeline.INDEFINITE);
        anim2.play();
    }

    // Helpers
    private void stopAnimations() {
        if (anim1 != null) { anim1.stop(); anim1 = null; }
        if (anim2 != null) { anim2.stop(); anim2 = null; }
    }

    private void clearCellHandlers() {
        for (Rectangle[] row : cells) {
            for (Rectangle cell : row) {
                cell.setOnMouseClicked(null);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}