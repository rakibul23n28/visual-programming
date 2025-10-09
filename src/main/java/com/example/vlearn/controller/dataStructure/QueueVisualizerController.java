package com.example.vlearn.controller.dataStructure;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class QueueVisualizerController {

    @FXML private HBox queueContainer;
    @FXML private TextField inputField;
    @FXML private Label messageLabel;
    @FXML private Label sizeLabel;
    @FXML private Label frontLabel;
    @FXML private Label rearLabel;

    private final List<Integer> queue = new ArrayList<>();
    private final int STEP_DURATION = 400;

    // ---------------- BUTTON HANDLERS ----------------

    @FXML
    private void handleEnqueue() {
        showValueField(true);
        try {
            int value = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                Platform.runLater(() -> {
                    resetAllBorders();
                    messageLabel.setText("Enqueuing value " + value + "...");
                });

                try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    queue.add(value);
                    HBox box = createQueueBox(value);
                    queueContainer.getChildren().add(box);
                    highlightBox(box, Color.LIMEGREEN);
                    updateSizeLabel();
                    updateFrontRearIndicators();
                    showMessage("Value " + value + " enqueued.");
                });
            }).start();
        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    @FXML
    private void handleDequeue() {
        showValueField(false);

        if (queue.isEmpty()) {
            showMessage("Queue is empty!");
            return;
        }

        new Thread(() -> {
            Platform.runLater(() -> {
                resetAllBorders();
                HBox box = (HBox) queueContainer.getChildren().get(0);
                highlightBox(box, Color.RED.darker());
                messageLabel.setText("Dequeuing front element...");
            });

            try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                int removedValue = queue.remove(0);
                queueContainer.getChildren().remove(0);
                updateSizeLabel();
                updateFrontRearIndicators();
                showMessage("Value " + removedValue + " dequeued.");
            });
        }).start();
    }

    @FXML
    private void handlePeek() {
        showValueField(false);

        if (queue.isEmpty()) {
            showMessage("Queue is empty!");
            return;
        }

        Platform.runLater(() -> {
            resetAllBorders();
            HBox box = (HBox) queueContainer.getChildren().get(0);
            highlightBox(box, Color.ORANGE);
            showMessage("Front of queue: " + queue.get(0));
        });
    }

    @FXML
    private void handleUpdateFront() {
        showValueField(true);

        if (queue.isEmpty()) {
            showMessage("Queue is empty!");
            return;
        }

        try {
            int newValue = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                Platform.runLater(() -> {
                    resetAllBorders();
                    HBox box = (HBox) queueContainer.getChildren().get(0);
                    highlightBox(box, Color.YELLOW);
                    messageLabel.setText("Updating front element...");
                });

                try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    queue.set(0, newValue);
                    HBox box = (HBox) queueContainer.getChildren().get(0);
                    Text text = (Text) box.getChildren().get(1);
                    text.setText(String.valueOf(newValue));
                    highlightBox(box, Color.GOLD);
                    showMessage("Front element updated to " + newValue + ".");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    @FXML
    private void handleFind() {
        showValueField(true);

        if (queue.isEmpty()) {
            showMessage("Queue is empty!");
            return;
        }

        try {
            int valueToFind = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                int foundIndex = -1;

                for (int i = 0; i < queue.size(); i++) {
                    final int idx = i;
                    HBox box = (HBox) queueContainer.getChildren().get(i);

                    Platform.runLater(() -> {
                        resetAllBorders();
                        highlightBox(box, Color.ORANGE);
                        vibrateBox(box);
                        messageLabel.setText("Checking element at index " + idx + "...");
                    });

                    try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}

                    if (queue.get(i) == valueToFind) {
                        foundIndex = i;
                        break;
                    }
                }

                final int index = foundIndex;
                Platform.runLater(() -> {
                    resetAllBorders();
                    if (index != -1) {
                        HBox box = (HBox) queueContainer.getChildren().get(index);
                        highlightBox(box, Color.YELLOW);
                        showMessage("Value " + valueToFind + " found at index " + index + "!");
                    } else {
                        showMessage("Value " + valueToFind + " not found in queue.");
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    @FXML
    private void handleReset() {
        queue.clear();
        queueContainer.getChildren().clear();
        updateSizeLabel();
        updateFrontRearIndicators();
        showMessage("Queue has been reset.");
    }

    // ---------------- HELPERS ----------------

    private HBox createQueueBox(int value) {
        Rectangle rect = new Rectangle(80, 40);
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setFill(Color.web("#1E90FF"));
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        Text text = new Text(String.valueOf(value));
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-weight: bold;");

        Label frontLbl = new Label("FRONT");
        frontLbl.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        frontLbl.setVisible(false);

        Label rearLbl = new Label("REAR");
        rearLbl.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
        rearLbl.setVisible(false);

        HBox box = new HBox(rect, text, frontLbl, rearLbl);
        box.setSpacing(5);
        box.setStyle("-fx-alignment: center;");
        return box;
    }

    private void highlightBox(HBox box, Color color) {
        Rectangle rect = (Rectangle) box.getChildren().get(0);
        rect.setStroke(color);
        rect.setStrokeWidth(5);
    }

    private void resetAllBorders() {
        for (Node node : queueContainer.getChildren()) {
            HBox box = (HBox) node;
            Rectangle rect = (Rectangle) box.getChildren().get(0);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        }
    }

    private void vibrateBox(HBox box) {
        final int distance = 10;
        final int cycles = 4;
        new Thread(() -> {
            for (int i = 0; i < cycles; i++) {
                int shift = (i % 2 == 0 ? distance : -distance);
                Platform.runLater(() -> box.setTranslateY(shift));
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
            Platform.runLater(() -> box.setTranslateY(0));
        }).start();
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
        new Thread(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                if (messageLabel.getText().equals(message)) {
                    messageLabel.setText("");
                }
            });
        }).start();
    }

    private void showValueField(boolean show) {
        inputField.setVisible(show);
        inputField.setManaged(show);
    }

    private void updateSizeLabel() {
        sizeLabel.setText("Size: " + queue.size());
    }

    private void updateFrontRearIndicators() {
        for (int i = 0; i < queueContainer.getChildren().size(); i++) {
            HBox box = (HBox) queueContainer.getChildren().get(i);
            Label frontLbl = (Label) box.getChildren().get(2);
            Label rearLbl = (Label) box.getChildren().get(3);
            frontLbl.setVisible(i == 0);
            rearLbl.setVisible(i == queue.size() - 1);
        }
    }
}
