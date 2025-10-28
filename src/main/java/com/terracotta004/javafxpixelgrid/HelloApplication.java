package com.terracotta004.javafxpixelgrid;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private static final int GRID_SIZE = 25;
    private static final int CELL_SIZE = 20;
    private static final double GAP = 1;

    private double phase = 0;
    private int tick = 0;
    private boolean anim1 = false;
    private boolean anim2 = false;

    @Override
    public void start(Stage stage) {
        double canvasSize = GRID_SIZE * (CELL_SIZE + GAP);
        Canvas canvas = new Canvas(canvasSize, canvasSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        BorderPane root = new BorderPane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("PixelGrid Optimized");
        stage.show();

        // Animation loop
        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate < 33_000_000) return; // ~30 fps
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                for (int r = 0; r < GRID_SIZE; r++) {
                    for (int c = 0; c < GRID_SIZE; c++) {
                        boolean fill;
                        if (anim1) {
                            double v = Math.sin(0.45 * r + 0.45 * c + phase);
                            fill = v > 0;
                        } else if (anim2) {
                            fill = ((r + c + tick) & 1) == 0;
                        } else {
                            fill = false;
                        }
                        gc.setFill(fill ? Color.BLACK : Color.WHITE);
                        gc.fillRect(c * (CELL_SIZE + GAP), r * (CELL_SIZE + GAP), CELL_SIZE, CELL_SIZE);
                    }
                }
                if (anim1) phase += 0.35;
                if (anim2) tick++;
                lastUpdate = now;
            }
        };
        timer.start();

        // Simple mouse toggles for animation modes
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DIGIT1 -> { anim1 = true; anim2 = false; }
                case DIGIT2 -> { anim1 = false; anim2 = true; }
                case DIGIT0 -> { anim1 = false; anim2 = false; }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
