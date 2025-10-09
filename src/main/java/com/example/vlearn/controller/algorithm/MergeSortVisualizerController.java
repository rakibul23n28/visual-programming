package com.example.vlearn.controller.algorithm;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class MergeSortVisualizerController {

    @FXML private Pane arrayPane;
    @FXML private TextField arrayInputField;
    @FXML private Label messageLabel;

    private List<Integer> arrayData = new ArrayList<>();
    private List<Rectangle> arrayBars = new ArrayList<>();
    private List<Label> valueLabels = new ArrayList<>();

    private boolean isSorting = false;
    private int delay = 200;

    // Colors
    private final Color DEFAULT_COLOR = Color.web("#4CC9F0");
    private final Color LEFT_COLOR = Color.web("#F72585");
    private final Color RIGHT_COLOR = Color.web("#7209B7");
    private final Color MERGE_COLOR = Color.web("#3A0CA3");
    private final Color SORTED_COLOR = Color.web("#4CAF50");
    private final Color COMPARE_COLOR = Color.web("#FFD700");

    @FXML
    public void initialize() {
        messageLabel.setText("Enter size or comma-separated values. Example: 10 or 5,2,8,1");
    }

    /* ---------------------- GENERATE ARRAY ---------------------- */
    @FXML
    public void handleGenerateArray() {
        if (isSorting) return;

        String input = arrayInputField.getText().trim();
        if (input.isEmpty()) {
            messageLabel.setText("Please enter numbers or size.");
            return;
        }

        try {
            if (input.matches("\\d+")) {
                generateRandomArray(Integer.parseInt(input));
            } else {
                arrayData = Arrays.stream(input.split(","))
                        .map(String::trim).map(Integer::parseInt).toList();
                drawArray();
            }
            messageLabel.setText("Array ready for sorting.");
        } catch (Exception e) {
            messageLabel.setText("Invalid input! Enter digits or comma-separated numbers.");
        }
    }

    private void generateRandomArray(int size) {
        Random rand = new Random();
        arrayData = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayData.add(rand.nextInt(200) + 20);
        }
        drawArray();
    }

    /* ---------------------- RESET FUNCTION ---------------------- */
    @FXML
    public void handleResetArray() {
        if (isSorting) return;
        handleGenerateArray();
        messageLabel.setText("Array reset to original state.");
    }

    /* ---------------------- CLEAR HIGHLIGHTS ---------------------- */
    @FXML
    public void handleClearHighlights() {
        if (arrayBars.isEmpty()) return;
        for (Rectangle bar : arrayBars) {
            bar.setFill(DEFAULT_COLOR);
        }
        messageLabel.setText("Highlights cleared.");
    }

    /* ---------------------- DRAW ARRAY ---------------------- */
    private void drawArray() {
        arrayPane.getChildren().clear();
        arrayBars.clear();
        valueLabels.clear();

        double width = arrayPane.getPrefWidth() / arrayData.size();
        int maxVal = Collections.max(arrayData);

        for (int i = 0; i < arrayData.size(); i++) {
            double height = (arrayData.get(i) / (double) maxVal) * arrayPane.getPrefHeight();

            Rectangle bar = new Rectangle(width - 5, height);
            bar.setX(i * width);
            bar.setY(arrayPane.getPrefHeight() - height + 100);
            bar.setFill(DEFAULT_COLOR);
            bar.setStroke(Color.BLACK);

            Label label = new Label(String.valueOf(arrayData.get(i)));
            label.setLayoutX(i * width + (width / 4));
            label.setLayoutY(bar.getY() - 20);
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #000;");

            arrayBars.add(bar);
            valueLabels.add(label);
            arrayPane.getChildren().addAll(bar, label);
        }
    }

    /* ---------------------- RUN SORT ---------------------- */
    @FXML
    public void handleRunMergeSort() {
        if (arrayData.isEmpty()) {
            messageLabel.setText("Generate an array first.");
            return;
        }
        if (isSorting) return;

        isSorting = true;
        messageLabel.setText("Sorting...");

        new Thread(() -> {
            try {
                mergeSort(0, arrayData.size() - 1);
                Platform.runLater(() -> {
                    colorSorted();
                    messageLabel.setText("✅ Sorting completed!");
                    isSorting = false;
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /* ---------------------- MERGE SORT LOGIC ---------------------- */
    private void mergeSort(int left, int right) throws InterruptedException {
        if (left >= right) return;

        int mid = (left + right) / 2;
        highlightRange(left, mid, LEFT_COLOR);
        mergeSort(left, mid);

        highlightRange(mid + 1, right, RIGHT_COLOR);
        mergeSort(mid + 1, right);

        merge(left, mid, right);
        resetColor(left, right);
    }

    private void merge(int left, int mid, int right) throws InterruptedException {
        List<Integer> leftPart = new ArrayList<>(arrayData.subList(left, mid + 1));
        List<Integer> rightPart = new ArrayList<>(arrayData.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftPart.size() && j < rightPart.size()) {
            highlightCompare(k);
            if (leftPart.get(i) <= rightPart.get(j)) {
                arrayData.set(k, leftPart.get(i++));
            } else {
                arrayData.set(k, rightPart.get(j++));
            }
            updateBar(k++);
            Thread.sleep(delay);
        }

        while (i < leftPart.size()) {
            arrayData.set(k, leftPart.get(i++));
            updateBar(k++);
            Thread.sleep(delay);
        }
        while (j < rightPart.size()) {
            arrayData.set(k, rightPart.get(j++));
            updateBar(k++);
            Thread.sleep(delay);
        }
    }

    /* ---------------------- HELPERS ---------------------- */
    private void updateBar(int index) {
        Platform.runLater(() -> {
            double width = arrayPane.getPrefWidth() / arrayData.size();
            int maxVal = Collections.max(arrayData);
            double height = (arrayData.get(index) / (double) maxVal) * arrayPane.getPrefHeight();

            Rectangle bar = arrayBars.get(index);
            bar.setHeight(height);
            bar.setY(arrayPane.getPrefHeight() - height + 100);
            bar.setFill(MERGE_COLOR);

            Label lbl = valueLabels.get(index);
            lbl.setText(String.valueOf(arrayData.get(index)));
            lbl.setLayoutY(bar.getY() - 20);
        });
    }

    private void highlightCompare(int idx) {
        Platform.runLater(() -> arrayBars.get(idx).setFill(COMPARE_COLOR));
    }

    private void highlightRange(int start, int end, Color c) {
        Platform.runLater(() -> {
            for (int i = start; i <= end; i++) {
                arrayBars.get(i).setFill(c);
            }
        });
    }

    private void resetColor(int start, int end) {
        Platform.runLater(() -> {
            for (int i = start; i <= end; i++) {
                arrayBars.get(i).setFill(DEFAULT_COLOR);
            }
        });
    }

    private void colorSorted() {
        for (Rectangle bar : arrayBars) bar.setFill(SORTED_COLOR);
    }
}
