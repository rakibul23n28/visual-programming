package com.example.vlearn.controller.algorithm;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BubbleSortVisualizerController {

    @FXML private Pane arrayPane;
    @FXML private TextField arrayInputField;
    @FXML private Label messageLabel;

    private List<Integer> arrayData;
    private List<Rectangle> arrayBars;
    private List<Label> arrayLabels; // 👈 New: Labels for values

    private final double MAX_BAR_HEIGHT = 300.0;
    private final double MAX_VALUE = 100.0;

    private final Color DEFAULT_COLOR = Color.LIGHTBLUE;
    private final Color COMPARE_COLOR = Color.YELLOW;
    private final Color SWAP_COLOR = Color.RED;
    private final Color SORTED_COLOR = Color.GREEN;

    private Timeline timeline;
    private int outerIndex = 0;
    private int innerIndex = 0;
    private boolean isSorting = false;

    @FXML
    public void initialize() {
        arrayData = new ArrayList<>();
        arrayBars = new ArrayList<>();
        arrayLabels = new ArrayList<>();
        messageLabel.setText("Enter a comma-separated array or a size for a random array.");
    }

    @FXML
    public void handleGenerateArray() {
        messageLabel.setText("");
        String input = arrayInputField.getText().trim();

        if (input.isEmpty()) {
            messageLabel.setText("Please enter numbers or size.");
            return;
        }

        if (input.matches("\\d+")) {
            generateRandomArray(Integer.parseInt(input));
            return;
        }

        try {
            arrayData = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (arrayData.isEmpty()) throw new IllegalArgumentException("Array cannot be empty.");

            arrayPane.getChildren().clear();
            arrayBars.clear();
            arrayLabels.clear();
            visualizeArray();

            messageLabel.setText("Array generated successfully. Ready to sort.");
        } catch (NumberFormatException e) {
            messageLabel.setText("Error: Invalid number format. Use comma-separated integers.");
        } catch (IllegalArgumentException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void generateRandomArray(int size) {
        if (size <= 0) {
            messageLabel.setText("Size must be positive.");
            return;
        }

        arrayData = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            arrayData.add(rand.nextInt((int)MAX_VALUE) + 1);
        }

        arrayPane.getChildren().clear();
        arrayBars.clear();
        arrayLabels.clear();
        visualizeArray();
        messageLabel.setText("Random array of size " + size + " generated.");
    }

    private void visualizeArray() {
        double paneWidth = arrayPane.getPrefWidth();
        double barWidth = paneWidth / arrayData.size() - 2;
        double xOffset = 1;
        double paneHeight = arrayPane.getPrefHeight();

        for (int i = 0; i < arrayData.size(); i++) {
            int value = arrayData.get(i);
            double barHeight = (value / MAX_VALUE) * MAX_BAR_HEIGHT;

            // --- Create bar ---
            Rectangle bar = new Rectangle(barWidth, barHeight);
            bar.setFill(DEFAULT_COLOR);
            bar.setStroke(Color.DARKBLUE);
            bar.setStrokeWidth(1);
            double x = xOffset + i * (barWidth + 2);
            double y = paneHeight - barHeight + 100;
            bar.setX(x);
            bar.setY(y);

            // --- Create label above bar ---
            Label label = new Label(String.valueOf(value));
            label.setLayoutX(x + barWidth / 4); // Center-ish alignment
            label.setLayoutY(y - 20);           // Slightly above the bar
            label.setTextFill(Color.BLACK);
            label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            arrayBars.add(bar);
            arrayLabels.add(label);
            arrayPane.getChildren().addAll(bar, label);
        }

        // Reset indices for sorting
        outerIndex = 0;
        innerIndex = 0;
        isSorting = false;
    }

    @FXML
    public void handleRunBubbleSort() {
        if (arrayData.isEmpty()) {
            messageLabel.setText("Please generate an array first.");
            return;
        }

        if (isSorting) return;

        isSorting = true;
        outerIndex = 0;
        innerIndex = 0;

        messageLabel.setText("Sorting in progress...");
        startTimelineSort();
    }

    private void startTimelineSort() {
        timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> {

            // --- Check if sorting is complete ---
            if (outerIndex >= arrayData.size() - 1) {
                timeline.stop();
                for (Rectangle bar : arrayBars) {
                    bar.setFill(SORTED_COLOR);
                }
                messageLabel.setText("Sorting complete! ✅");
                isSorting = false;
                return;
            }

            // Reset previous comparison bars
            if (innerIndex > 0) {
                arrayBars.get(innerIndex - 1).setFill(DEFAULT_COLOR);
            }

            // Highlight current comparison
            arrayBars.get(innerIndex).setFill(COMPARE_COLOR);
            arrayBars.get(innerIndex + 1).setFill(SWAP_COLOR);

            // Swap if necessary
            if (arrayData.get(innerIndex) > arrayData.get(innerIndex + 1)) {
                swapData(innerIndex, innerIndex + 1);
                swapBars(innerIndex, innerIndex + 1);
                swapLabels(innerIndex, innerIndex + 1);
            }

            // Move inner loop
            innerIndex++;

            // Check if end of inner loop
            if (innerIndex >= arrayData.size() - 1 - outerIndex) {
                arrayBars.get(arrayData.size() - 1 - outerIndex).setFill(SORTED_COLOR);
                outerIndex++;
                innerIndex = 0;
            }

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void swapData(int i, int j) {
        int temp = arrayData.get(i);
        arrayData.set(i, arrayData.get(j));
        arrayData.set(j, temp);
    }

    private void swapBars(int i, int j) {
        Rectangle bar1 = arrayBars.get(i);
        Rectangle bar2 = arrayBars.get(j);

        // Swap references
        arrayBars.set(i, bar2);
        arrayBars.set(j, bar1);

        // Update X positions
        double x1 = bar1.getX();
        double x2 = bar2.getX();
        bar1.setX(x2);
        bar2.setX(x1);
    }

    private void swapLabels(int i, int j) {
        Label label1 = arrayLabels.get(i);
        Label label2 = arrayLabels.get(j);

        // Swap in list
        arrayLabels.set(i, label2);
        arrayLabels.set(j, label1);

        // Update positions
        double x1 = label1.getLayoutX();
        double x2 = label2.getLayoutX();
        label1.setLayoutX(x2);
        label2.setLayoutX(x1);
    }

    @FXML
    public void handleResetArray() {
        if (timeline != null) timeline.stop();
        handleGenerateArray();
        messageLabel.setText("Array reset to initial state.");
    }

    @FXML
    public void handleClearHighlights() {
        if (!arrayBars.isEmpty()) {
            arrayBars.forEach(bar -> bar.setFill(DEFAULT_COLOR));
            messageLabel.setText("Highlights cleared.");
        }
    }
}
