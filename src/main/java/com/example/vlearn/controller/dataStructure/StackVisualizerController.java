package com.example.vlearn.controller.dataStructure;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class StackVisualizerController {

    @FXML private VBox stackContainer;
    @FXML private TextField inputField;
    @FXML private Label messageLabel;
    @FXML private Label sizeLabel;

    private final List<Integer> stack = new ArrayList<>();
    private final int STEP_DURATION = 400;

    // ---------------- BUTTON HANDLERS ----------------

    @FXML
    private void handlePush() {
        showValueField(true);

        try {
            int value = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                Platform.runLater(() -> {
                    resetAllBorders(); // Reset previous highlights
                    messageLabel.setText("Pushing value " + value + "...");
                });

                try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    stack.add(value);
                    VBox box = createStackBox(value);
                    stackContainer.getChildren().add(box);
                    highlightBox(box, Color.LIMEGREEN);
                    updateSizeLabel();
                    updateTopIndicator();
                    showMessage("Value " + value + " pushed onto stack.");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    @FXML
    private void handlePop() {
        showValueField(false);

        if (stack.isEmpty()) {
            showMessage("Stack is empty!");
            return;
        }

        new Thread(() -> {
            int topIndex = stack.size() - 1;
            Platform.runLater(() -> {
                resetAllBorders();
                VBox box = (VBox) stackContainer.getChildren().get(topIndex);
                highlightBox(box, Color.RED.darker());
                messageLabel.setText("Popping top element...");
            });

            try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                int poppedValue = stack.remove(stack.size() - 1);
                stackContainer.getChildren().remove(stackContainer.getChildren().size() - 1);
                updateSizeLabel();
                updateTopIndicator();
                showMessage("Value " + poppedValue + " popped from stack.");
            });
        }).start();
    }

    @FXML
    private void handlePeek() {
        showValueField(false);

        if (stack.isEmpty()) {
            showMessage("Stack is empty!");
            return;
        }

        int topIndex = stack.size() - 1;
        Platform.runLater(() -> {
            resetAllBorders();
            VBox box = (VBox) stackContainer.getChildren().get(topIndex);
            highlightBox(box, Color.ORANGE);
            showMessage("Top of stack: " + stack.get(topIndex));
        });
    }

    @FXML
    private void handleUpdateTop() {
        showValueField(true);

        if (stack.isEmpty()) {
            showMessage("Stack is empty!");
            return;
        }

        try {
            int newValue = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                int topIndex = stack.size() - 1;
                Platform.runLater(() -> {
                    resetAllBorders();
                    VBox box = (VBox) stackContainer.getChildren().get(topIndex);
                    highlightBox(box, Color.YELLOW);
                    messageLabel.setText("Updating top element...");
                });

                try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    stack.set(topIndex, newValue);
                    VBox box = (VBox) stackContainer.getChildren().get(topIndex);
                    Text text = (Text) box.getChildren().get(1);
                    text.setText(String.valueOf(newValue));
                    highlightBox(box, Color.GOLD);
                    showMessage("Top element updated to " + newValue + ".");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    @FXML
    private void handleFind() {
        showValueField(true);

        if (stack.isEmpty()) {
            showMessage("Stack is empty!");
            return;
        }

        try {
            int valueToFind = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                int foundIndex = -1;

                for (int i = stack.size() - 1; i >= 0; i--) {
                    final int idx = i;
                    VBox box = (VBox) stackContainer.getChildren().get(i);

                    Platform.runLater(() -> {
                        resetAllBorders();
                        highlightBox(box, Color.ORANGE);
                        vibrateBox(box);
                        messageLabel.setText("Checking element at index " + idx + "...");
                    });

                    try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}

                    if (stack.get(i) == valueToFind) {
                        foundIndex = i;
                        break;
                    }
                }

                final int index = foundIndex;
                Platform.runLater(() -> {
                    resetAllBorders();
                    if (index != -1) {
                        VBox box = (VBox) stackContainer.getChildren().get(index);
                        highlightBox(box, Color.YELLOW);
                        showMessage("Value " + valueToFind + " found at index " + index + "!");
                    } else {
                        showMessage("Value " + valueToFind + " not found in stack.");
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    @FXML
    private void handleReset() {
        stack.clear();
        stackContainer.getChildren().clear();
        updateSizeLabel();
        updateTopIndicator();
        showMessage("Stack has been reset.");
    }

    // ---------------- HELPERS ----------------

    private VBox createStackBox(int value) {
        Rectangle rect = new Rectangle(80, 40);
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setFill(Color.web("#1E90FF"));
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        Text text = new Text(String.valueOf(value));
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-weight: bold;");

        Label topLabel = new Label("TOP");
        topLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        topLabel.setVisible(false);

        VBox box = new VBox(rect, text, topLabel);
        box.setSpacing(2);
        box.setStyle("-fx-alignment: center;");
        return box;
    }

    private void highlightBox(VBox box, Color color) {
        Rectangle rect = (Rectangle) box.getChildren().get(0);
        rect.setStroke(color);
        rect.setStrokeWidth(5);
    }

    // ✅ Reset all borders to default
    private void resetAllBorders() {
        for (Node node : stackContainer.getChildren()) {
            VBox box = (VBox) node;
            Rectangle rect = (Rectangle) box.getChildren().get(0);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        }
    }

    private void vibrateBox(VBox box) {
        final int distance = 10;
        final int cycles = 4;
        new Thread(() -> {
            for (int i = 0; i < cycles; i++) {
                int shift = (i % 2 == 0 ? distance : -distance);
                Platform.runLater(() -> box.setTranslateX(shift));
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
            Platform.runLater(() -> box.setTranslateX(0));
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
        sizeLabel.setText("Size: " + stack.size());
    }

    private void updateTopIndicator() {
        for (int i = 0; i < stackContainer.getChildren().size(); i++) {
            VBox box = (VBox) stackContainer.getChildren().get(i);
            Label topLabel = (Label) box.getChildren().get(2);
            topLabel.setVisible(i == stack.size() - 1);
        }
    }
}
