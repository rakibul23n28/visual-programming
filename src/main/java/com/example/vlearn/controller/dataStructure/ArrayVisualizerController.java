package com.example.vlearn.controller.dataStructure;

import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ArrayVisualizerController {
    @FXML private FlowPane arrayContainer;
    @FXML private TextField inputField;
    @FXML private TextField indexField;
    @FXML private Label messageLabel;
    @FXML private HBox indexBox;
    @FXML private Label sizeLabel;
    @FXML private ScrollPane arrayScrollPane;

    private List<Integer> array = new ArrayList<>();
    private final int STEP_DURATION = 400;

    // ----------------- BUTTON HANDLERS -----------------

    @FXML
    private void handleInsert() {
        showIndexField(true);
        showValueField(true);
        try {
            int value = parseInput(inputField.getText());
            int index = parseIndex(indexField.getText(), array.size());
            if (index < 0 || index > array.size()) {
                showMessage("Index out of bounds!");
                return;
            }

            clearInputs();

            new Thread(() -> {
                Platform.runLater(() -> messageLabel.setText("Preparing to insert " + value + " at index " + index + "..."));

                // Shift elements right
                for (int i = array.size() - 1; i >= index; i--) {
                    final int currentIndex = i;
                    Platform.runLater(() -> {
                        messageLabel.setText("Shifting element at index " + currentIndex + " to the right.");
                        if (currentIndex < arrayContainer.getChildren().size()) {
                            highlightBoxTemporary((VBox) arrayContainer.getChildren().get(currentIndex), Color.LIGHTBLUE, STEP_DURATION);
                        }
                    });
                    try { Thread.sleep(STEP_DURATION + 100); } catch (InterruptedException ignored) {}
                }

                Platform.runLater(() -> {
                    array.add(index, value);
                    renderArray();
                    if (array.size() > index) {
                        VBox newBox = (VBox) arrayContainer.getChildren().get(index);
                        highlightBox(newBox, Color.LIMEGREEN);
                        showMessage("Value " + value + " inserted at index " + index + ".");
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter valid numbers and index!");
        }
    }

    @FXML
    private void handleAppend() {
        showIndexField(false);
        showValueField(true);
        try {
            int value = parseInput(inputField.getText());
            clearInputs();

            array.add(value);
            renderArray();
            int index = array.size() - 1;
            if (index >= 0) {
                VBox newBox = (VBox) arrayContainer.getChildren().get(index);
                highlightBox(newBox, Color.LIMEGREEN);
            }
            showMessage("Value " + value + " appended at index " + index + ".");

        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleDelete() {
        showIndexField(true);
        showValueField(false);

        try {
            int index = parseIndex(indexField.getText(), -1);
            if (index < 0 || index >= array.size()) {
                showMessage("Index out of bounds!");
                return;
            }

            clearInputs();

            new Thread(() -> {
                Platform.runLater(() -> messageLabel.setText("Accessing element at index " + index + " for deletion..."));

                Platform.runLater(() -> {
                    VBox boxToDelete = (VBox) arrayContainer.getChildren().get(index);
                    highlightBox(boxToDelete, Color.RED.darker());
                    showMessage("Marking element at index " + index + " for deletion.");
                });
                try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    int deletedValue = array.remove(index);
                    renderArray();
                    showMessage("Value " + deletedValue + " deleted from index " + index + ". Elements shifted left.");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid index!");
        }
    }

    @FXML
    private void handlePop() {
        showIndexField(false);
        showValueField(false);

        if (array.isEmpty()) {
            showMessage("Array is empty!");
            return;
        }

        int index = array.size() - 1;

        new Thread(() -> {
            Platform.runLater(() -> messageLabel.setText("Accessing last element for pop..."));

            Platform.runLater(() -> {
                VBox boxToPop = (VBox) arrayContainer.getChildren().get(index);
                highlightBox(boxToPop, Color.RED.darker());
                showMessage("Marking last element at index " + index + " for pop.");
            });
            try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                int poppedValue = array.remove(index);
                renderArray();
                showMessage("Value " + poppedValue + " popped from the end.");
            });
        }).start();
    }

    @FXML
    private void handleUpdate() {
        showValueField(true);
        showIndexField(true);

        try {
            int index = parseIndex(indexField.getText(), -1);
            int value = parseInput(inputField.getText());
            if (index < 0 || index >= array.size()) {
                showMessage("Index out of bounds!");
                return;
            }

            clearInputs();

            new Thread(() -> {
                Platform.runLater(() -> messageLabel.setText("Accessing index " + index + " for update..."));

                Platform.runLater(() -> {
                    VBox boxToUpdate = (VBox) arrayContainer.getChildren().get(index);
                    highlightBox(boxToUpdate, Color.YELLOW.darker());
                });
                try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    array.set(index, value);
                    VBox box = (VBox) arrayContainer.getChildren().get(index);
                    Text text = (Text) box.getChildren().get(1);
                    text.setText(String.valueOf(value));
                    highlightBox(box, Color.GOLD);
                    showMessage("Index " + index + " successfully updated to " + value + ".");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter valid numbers!");
        }
    }

    @FXML
    private void handleFind() {
        showIndexField(false);
        showValueField(true);

        try {
            int valueToFind = parseInput(inputField.getText());
            clearInputs();

            new Thread(() -> {
                Platform.runLater(() -> messageLabel.setText("Starting search for value " + valueToFind + "..."));
                int foundIndex = -1;

                for (int i = 0; i < array.size(); i++) {
                    final int currentIndex = i;
                    final int currentValue = array.get(i);

                    Platform.runLater(() -> {
                        messageLabel.setText("Checking index " + currentIndex + " (Value: " + currentValue + ")");
                        highlightBoxTemporary((VBox) arrayContainer.getChildren().get(currentIndex), Color.ORANGE, STEP_DURATION);
                    });

                    try { Thread.sleep(STEP_DURATION + 100); } catch (InterruptedException ignored) {}

                    if (currentValue == valueToFind) {
                        foundIndex = currentIndex;
                        break;
                    }
                }

                final int finalIndex = foundIndex;
                Platform.runLater(() -> {
                    if (finalIndex != -1) {
                        showMessage("Value " + valueToFind + " found at index " + finalIndex + "!");
                        highlightBox((VBox) arrayContainer.getChildren().get(finalIndex), Color.LIMEGREEN);
                    } else {
                        showMessage("Value " + valueToFind + " not found in the array.");
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleDeleteByValue() {
        showIndexField(false);
        showValueField(true);

        try {
            int valueToDelete = parseInput(inputField.getText());
            clearInputs();

            new Thread(() -> {
                Platform.runLater(() -> messageLabel.setText("Searching for value " + valueToDelete + " to delete..."));
                int foundIndex = -1;

                for (int i = 0; i < array.size(); i++) {
                    final int currentIndex = i;
                    final int currentValue = array.get(i);

                    Platform.runLater(() -> {
                        messageLabel.setText("Checking index " + currentIndex + " (Value: " + currentValue + ")");
                        highlightBoxTemporary((VBox) arrayContainer.getChildren().get(currentIndex), Color.ORANGE, STEP_DURATION);
                    });

                    try { Thread.sleep(STEP_DURATION + 100); } catch (InterruptedException ignored) {}

                    if (currentValue == valueToDelete) {
                        foundIndex = currentIndex;
                        break;
                    }
                }

                final int finalIndex = foundIndex;

                if (finalIndex != -1) {
                    Platform.runLater(() -> {
                        VBox boxToDelete = (VBox) arrayContainer.getChildren().get(finalIndex);
                        highlightBox(boxToDelete, Color.RED.darker());
                        showMessage("Deleting value " + valueToDelete + " at index " + finalIndex + "...");
                    });

                    try { Thread.sleep(STEP_DURATION * 2); } catch (InterruptedException ignored) {}

                    Platform.runLater(() -> {
                        array.remove(finalIndex);
                        renderArray();
                        showMessage("Value " + valueToDelete + " deleted from index " + finalIndex + ".");
                    });

                } else {
                    Platform.runLater(() -> showMessage("Value " + valueToDelete + " not found in the array."));
                }

            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleReset() {
        array.clear();
        arrayContainer.getChildren().clear();
        sizeLabel.setText("Size: 0");
        messageLabel.setText("");
        showIndexField(false);
        showValueField(true);
        clearInputs();
    }

    // ----------------- HELPER METHODS -----------------

    private int parseInput(String text) throws NumberFormatException {
        if (text == null || text.isEmpty()) throw new NumberFormatException();
        return Integer.parseInt(text);
    }

    private int parseIndex(String text, int defaultValue) throws NumberFormatException {
        if (text == null || text.isEmpty()) return defaultValue;
        return Integer.parseInt(text);
    }

    private void clearInputs() {
        inputField.clear();
        indexField.clear();
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

    private void showIndexField(boolean show) {
        indexBox.setVisible(show);
        indexBox.setManaged(show);
    }

    private void showValueField(boolean show) {
        inputField.setVisible(show);
        inputField.setManaged(show);
    }

    private VBox createArrayBox(int value, int index) {
        Rectangle rect = new Rectangle(60, 60);
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setFill(Color.web("#F0F8FF"));
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        Text text = new Text(String.valueOf(value));
        text.setFill(Color.BLACK); // Set text color
        text.setStyle("-fx-font-weight: bold;"); // Make text bold

        VBox vbox = new VBox(rect, text);
        vbox.setSpacing(0);
        vbox.setStyle("-fx-alignment: center;");
        Tooltip.install(vbox, new Tooltip("Index: " + index));
        return vbox;
    }


    private void highlightBox(VBox box, Color color) {
        Rectangle rect = (Rectangle) box.getChildren().get(0);
        rect.setStroke(color);
        rect.setStrokeWidth(5);
    }

    private void highlightBoxTemporary(VBox box, Color color, int duration) {
        Rectangle rect = (Rectangle) box.getChildren().get(0);
        FillTransition ft = new FillTransition(Duration.millis(duration / 2), rect, (Color) rect.getFill(), color);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        rect.setStroke(color.darker());
        rect.setStrokeWidth(4);
        ft.setOnFinished(event -> {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        });
        ft.play();
    }

    private void renderArray() {
        arrayContainer.getChildren().clear();
        for (int i = 0; i < array.size(); i++) {
            VBox box = createArrayBox(array.get(i), i);
            arrayContainer.getChildren().add(box);
        }
        sizeLabel.setText("Size: " + array.size());
        Platform.runLater(() -> arrayScrollPane.setHvalue(1.0)); // scroll to end
    }
}
