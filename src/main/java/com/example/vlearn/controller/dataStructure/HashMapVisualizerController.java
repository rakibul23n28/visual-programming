package com.example.vlearn.controller.dataStructure;

import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HashMapVisualizerController {

    @FXML private Pane hashMapPane;
    @FXML private TextField keyField;
    @FXML private TextField valueField;
    @FXML private Label messageLabel;
    @FXML private Label sizeLabel;
    @FXML private Label bucketsLabel;

    // --- Visualization Constants ---
    private static final int STEP_DURATION = 500;
    private static final double BUCKET_LABEL_Y = 10;
    private static final double BUCKET_START_Y = 50;
    private static final double BUCKET_SPACING_X = 120; // Increased spacing for better visibility
    private static final double ENTRY_SPACING_Y = 80;   // Vertical space between entries in a chain
    private static final double PANE_PADDING = 50;      // Padding on the right for the Pane width calculation

    // --- Bucket Colors for Distinction ---
    private static final Color[] BUCKET_COLORS = new Color[] {
            Color.web("#FF6347"), // Tomato (Red-like)
            Color.web("#4682B4"), // SteelBlue
            Color.web("#3CB371"), // MediumSeaGreen
            Color.web("#DAA520"), // Goldenrod
            Color.web("#9370DB"), // MediumPurple
            Color.web("#00CED1"), // DarkTurquoise
            Color.web("#FF69B4"), // HotPink
            Color.web("#B8860B"), // DarkGoldenrod
            Color.web("#6495ED"), // CornflowerBlue
            Color.web("#A9A9A9")  // DarkGray
    };


    private List<List<HashEntry>> buckets;
    private int size = 0;
    private int numBuckets = 16; // Start with 16

    // ----------------- HASH ENTRY -----------------
    private static class HashEntry {
        String key, value;
        VBox box;

        HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashMapVisualizerController() {
        initBuckets();
    }

    private void initBuckets() {
        buckets = new ArrayList<>();
        for (int i = 0; i < numBuckets; i++) buckets.add(new ArrayList<>());
    }

    // ----------------- BUTTON HANDLERS -----------------
    @FXML
    private void handleInsert() {
        String key = keyField.getText().trim();
        String value = valueField.getText().trim();
        if (key.isEmpty() || value.isEmpty()) {
            showMessage("Enter both key and value!");
            return;
        }
        keyField.clear();
        valueField.clear();

        new Thread(() -> {
            Platform.runLater(() -> showMessage("Inserting (" + key + ", " + value + ")..."));
            insertEntry(key, value);
            Platform.runLater(this::renderHashMap);
            Platform.runLater(() -> showMessage("Inserted (" + key + ", " + value + ")."));
        }).start();
    }

    private void insertEntry(String key, String value) {
        int index = Math.abs(key.hashCode()) % numBuckets;
        List<HashEntry> bucket = buckets.get(index);

        for (HashEntry entry : bucket) {
            highlightNode(entry.box, Color.web("#00BFFF"));
            sleep(STEP_DURATION);
            if (entry.key.equals(key)) {
                Platform.runLater(() -> {
                    entry.value = value;
                    updateEntryBoxText(entry.box, entry.key, entry.value); // Update text on box
                });
                return;
            }
        }

        HashEntry newEntry = new HashEntry(key, value);
        // Ensure VBox creation happens on the JavaFX thread
        Platform.runLater(() -> newEntry.box = createEntryBox(key, value, index));
        sleep(50); // Small pause for JavaFX thread to catch up

        bucket.add(newEntry);
        size++;
        highlightNode(newEntry.box, Color.web("#00FF7F"));
        sleep(STEP_DURATION);
    }

    // handleDelete, handleFind, handleUpdate, handleResize, handleClear, handleReset...
    // ... (omitted for brevity, as they remain mostly the same, relying on renderHashMap)

    @FXML
    private void handleDelete() {
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            showMessage("Enter a key!");
            return;
        }
        keyField.clear();

        new Thread(() -> {
            AtomicBoolean found = new AtomicBoolean(false);
            int index = Math.abs(key.hashCode()) % numBuckets;
            if (index >= numBuckets) return;
            List<HashEntry> bucket = buckets.get(index);

            HashEntry entryToDelete = null;
            for (HashEntry entry : bucket) {
                highlightNode(entry.box, Color.web("#FFA500"));
                sleep(STEP_DURATION);
                if (entry.key.equals(key)) {
                    entryToDelete = entry;
                    size--;
                    found.set(true);
                    break;
                }
            }

            if (entryToDelete != null) {
                bucket.remove(entryToDelete);
            }

            Platform.runLater(() -> {
                if (found.get()) {
                    renderHashMap();
                    showMessage("Deleted key " + key + ".");
                } else showMessage("Key " + key + " not found!");
            });
        }).start();
    }

    @FXML
    private void handleFind() {
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            showMessage("Enter a key!");
            return;
        }
        keyField.clear();

        new Thread(() -> {
            int index = Math.abs(key.hashCode()) % numBuckets;
            if (index >= numBuckets) return;
            List<HashEntry> bucket = buckets.get(index);
            boolean found = false;

            for (HashEntry entry : bucket) {
                highlightNode(entry.box, Color.web("#FFFF00"));
                sleep(STEP_DURATION);
                if (entry.key.equals(key)) {
                    highlightNode(entry.box, Color.web("#00FF00"));
                    found = true;
                    break;
                }
            }

            boolean finalFound = found;
            Platform.runLater(() -> {
                if (finalFound) showMessage("Found key " + key + ".");
                else showMessage("Key " + key + " not found!");
            });
        }).start();
    }

    @FXML
    private void handleUpdate() {
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            showMessage("Enter a key to update!");
            return;
        }
        keyField.clear();

        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Value");
            dialog.setHeaderText("Update Value for Key: " + key);
            dialog.setContentText("Enter new value:");
            Optional<String> result = dialog.showAndWait();

            if (result.isEmpty()) return;
            String newValue = result.get().trim();
            if (newValue.isEmpty()) return;

            new Thread(() -> {
                int index = Math.abs(key.hashCode()) % numBuckets;
                if (index >= numBuckets) return;
                List<HashEntry> bucket = buckets.get(index);
                boolean found = false;

                for (HashEntry entry : bucket) {
                    highlightNode(entry.box, Color.web("#FFA500"));
                    sleep(STEP_DURATION);
                    if (entry.key.equals(key)) {
                        Platform.runLater(() -> {
                            entry.value = newValue;
                            updateEntryBoxText(entry.box, entry.key, entry.value);
                            highlightNode(entry.box, Color.web("#00FF7F"));
                        });
                        found = true;
                        break;
                    }
                }

                boolean finalFound = found;
                Platform.runLater(() -> {
                    if (finalFound) {
                        renderHashMap();
                        showMessage("Updated key " + key + " to new value.");
                    } else showMessage("Key " + key + " not found!");
                });
            }).start();
        });
    }

    @FXML
    private void handleResize() {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(numBuckets * 2));
            dialog.setTitle("Resize HashMap");
            dialog.setHeaderText("Enter new number of buckets:");
            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) return;

            try {
                int newBucketCount = Integer.parseInt(result.get());
                if (newBucketCount <= 0) {
                    showMessage("Bucket count must be positive!");
                    return;
                }

                new Thread(() -> {
                    Platform.runLater(() -> showMessage("Resizing HashMap to " + newBucketCount + " buckets..."));
                    List<HashEntry> allEntries = new ArrayList<>();
                    for (List<HashEntry> bucket : buckets) allEntries.addAll(bucket);

                    numBuckets = newBucketCount;
                    initBuckets();
                    size = 0;

                    // Re-create VBoxes with the correct new bucket color index
                    List<HashEntry> rehashedEntries = new ArrayList<>();
                    for (HashEntry entry : allEntries) {
                        int index = Math.abs(entry.key.hashCode()) % numBuckets;
                        // Re-create VBox for new color
                        Platform.runLater(() -> entry.box = createEntryBox(entry.key, entry.value, index));
                        buckets.get(index).add(entry);
                        size++;
                    }
                    sleep(100); // Pause for UI creation

                    Platform.runLater(this::renderHashMap);
                    Platform.runLater(() -> showMessage("Resize complete."));
                }).start();

            } catch (NumberFormatException e) {
                showMessage("Enter a valid number!");
            }
        });
    }

    @FXML
    private void handleClear() {
        buckets.forEach(List::clear);
        size = 0;
        Platform.runLater(this::renderHashMap);
        showMessage("HashMap cleared.");
    }

    @FXML
    private void handleReset() {
        buckets.forEach(List::clear);
        size = 0;
        numBuckets = 16;
        initBuckets();
        Platform.runLater(this::renderHashMap);
        showMessage("HashMap reset.");
    }

    // ----------------- RENDERING (UPDATED FOR SCROLLING AND COLOR) -----------------
    private void renderHashMap() {
        hashMapPane.getChildren().clear(); // Clear all nodes and lines

        // Calculate required width for all buckets
        double requiredWidth = (numBuckets * BUCKET_SPACING_X) + PANE_PADDING;

        // IMPORTANT: Update the Pane's preferred width to enable scrolling in ScrollPane (if used)
        hashMapPane.setPrefWidth(requiredWidth);

        for (int i = 0; i < numBuckets; i++) { // Iterate through ALL buckets
            List<HashEntry> bucket = buckets.get(i);

            // Get bucket color (cycles through defined colors)
            Color bucketColor = BUCKET_COLORS[i % BUCKET_COLORS.length];

            // 1. Draw Bucket Index Label
            double bucketX = PANE_PADDING + i * BUCKET_SPACING_X;
            Text indexText = new Text("Bucket [" + i + "]");
            indexText.setLayoutX(bucketX - 15);
            indexText.setLayoutY(BUCKET_LABEL_Y);
            indexText.setFill(bucketColor); // Color the text label
            indexText.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            hashMapPane.getChildren().add(indexText);

            // 2. Draw Entry Chain
            double currentY = BUCKET_START_Y;
            VBox previousBox = null;

            for (HashEntry entry : bucket) {
                VBox box = entry.box;

                // Set position (all in the same column, stepping down)
                box.setLayoutX(bucketX);
                box.setLayoutY(currentY);

                // Ensure the entry box is updated with the correct bucket color
                updateEntryBoxColor(box, bucketColor);

                // Add entry box to pane
                if (!hashMapPane.getChildren().contains(box)) {
                    hashMapPane.getChildren().add(box);
                }

                // Draw connecting line from previous node (simulating linked list/chaining)
                if (previousBox != null) {
                    Line line = createConnectingLine(previousBox, box, bucketColor);
                    hashMapPane.getChildren().add(line);
                }

                previousBox = box;
                currentY += ENTRY_SPACING_Y; // Move to the position for the next entry
            }
        }

        // Update status labels
        sizeLabel.setText("Size: " + size);
        bucketsLabel.setText("Buckets: " + numBuckets);
    }

    // Pass bucket index for initial node coloring
    private VBox createEntryBox(String key, String value, int bucketIndex) {
        Color bucketColor = BUCKET_COLORS[bucketIndex % BUCKET_COLORS.length];

        Circle circle = new Circle(30, Color.web("#E6E6FA")); // 🎨 Base lavender
        circle.setStroke(bucketColor); // Use bucket color for outline
        circle.setStrokeWidth(2);
        Text text = new Text(key + ":" + value);
        text.setFill(Color.web("#000000"));
        text.setStyle("-fx-font-weight: bold;");
        VBox vbox = new VBox(circle, text);
        vbox.setSpacing(-15);
        vbox.setStyle("-fx-alignment: center;");
        return vbox;
    }

    // Utility to update the box color after creation (e.g., after a resize)
    private void updateEntryBoxColor(VBox box, Color bucketColor) {
        if (box.getChildren().size() > 0 && box.getChildren().get(0) instanceof Circle) {
            Circle circle = (Circle) box.getChildren().get(0);
            circle.setStroke(bucketColor);
        }
    }

    // Pass color to the line
    private Line createConnectingLine(VBox startBox, VBox endBox, Color color) {
        double startX = startBox.getLayoutX() + 30;
        double startY = startBox.getLayoutY() + 60;
        double endX = endBox.getLayoutX() + 30;
        double endY = endBox.getLayoutY();

        Line line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(2);
        line.setStroke(color.deriveColor(0, 1.0, 0.7, 1.0)); // Darker shade of bucket color for line
        return line;
    }

    private void updateEntryBoxText(VBox box, String key, String value) {
        if (box.getChildren().size() > 1 && box.getChildren().get(1) instanceof Text) {
            Text textNode = (Text) box.getChildren().get(1);
            textNode.setText(key + ":" + value);
        }
    }


    // ----------------- UTILITIES -----------------
    private void highlightNode(VBox box, Color color) {
        if (box == null) return;

        Platform.runLater(() -> {
            Circle circle = (Circle) box.getChildren().get(0);

            // Store the current stroke color, which is the bucket color
            Color originalStrokeColor = (Color) circle.getStroke();
            Color originalFillColor = (Color) circle.getFill();

            // Highlight animation
            FillTransition ft = new FillTransition(Duration.millis(STEP_DURATION), circle, originalFillColor, color);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);

            // Restore original fill color after animation
            ft.setOnFinished(e -> circle.setFill(originalFillColor));

            ft.play();
            circle.setStroke(color); // Highlight stroke during operation

            // Restore original stroke color after animation is done
            new Thread(() -> {
                sleep(STEP_DURATION * 2);
                Platform.runLater(() -> circle.setStroke(originalStrokeColor));
            }).start();
        });
    }

    private void showMessage(String message) {
        Platform.runLater(() -> messageLabel.setText(message));
        new Thread(() -> {
            sleep(3000);
            Platform.runLater(() -> {
                if (messageLabel.getText().equals(message)) messageLabel.setText("");
            });
        }).start();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}