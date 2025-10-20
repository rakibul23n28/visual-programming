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

public class BinaryTreeVisualizerController {

    @FXML private Pane treePane;
    @FXML private TextField inputField;
    @FXML private Label messageLabel;
    @FXML private Label sizeLabel;
    @FXML private Label heightLabel;

    private TreeNode root;
    private int size = 0;
    private final int STEP_DURATION = 500;

    // ----------------- TREE NODE CLASS -----------------
    private static class TreeNode {
        int value;
        TreeNode left, right;
        VBox box;

        TreeNode(int value) {
            this.value = value;
        }
    }

    // ----------------- BUTTON HANDLERS -----------------

    @FXML
    private void handleInsert() {
        try {
            int value = Integer.parseInt(inputField.getText());
            inputField.clear();
            new Thread(() -> {
                Platform.runLater(() -> showMessage("Inserting " + value + "..."));
                root = insertNode(root, value);
                size++;
                Platform.runLater(this::renderTree);
                Platform.runLater(() -> showMessage("Inserted " + value + "."));
            }).start();
        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    private TreeNode insertNode(TreeNode node, int value) {
        if (node == null) {
            TreeNode newNode = new TreeNode(value);
            newNode.box = createNodeBox(value);
            highlightNode(newNode.box, Color.web("#00FF7F")); // 💚 Bright Spring Green
            sleep(STEP_DURATION);
            return newNode;
        }
        highlightNode(node.box, Color.web("#00BFFF")); // 💙 Deep Sky Blue
        sleep(STEP_DURATION);

        if (value < node.value) node.left = insertNode(node.left, value);
        else node.right = insertNode(node.right, value);

        return node;
    }

    @FXML
    private void handleDelete() {
        try {
            int value = Integer.parseInt(inputField.getText());
            inputField.clear();

            new Thread(() -> {
                AtomicBoolean found = new AtomicBoolean(false);

                // Perform deletion
                TreeNode newRoot = deleteNode(root, value, found);

                Platform.runLater(() -> {
                    if (found.get()) {
                        root = newRoot; // ✅ Always reassign root
                        size--;
                        renderTree(); // ✅ Re-render tree UI
                        showMessage("Deleted " + value + ".");
                    } else {
                        showMessage("Value " + value + " not found!");
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }


    private TreeNode deleteNode(TreeNode node, int value, AtomicBoolean found) {
        if (node == null) return null;

        highlightNode(node.box, Color.web("#FFA500")); // 🟧 Bright Orange
        sleep(STEP_DURATION);

        if (value < node.value) node.left = deleteNode(node.left, value, found);
        else if (value > node.value) node.right = deleteNode(node.right, value, found);
        else {
            found.set(true);
            highlightNode(node.box, Color.web("#FF1744")); // ❤️ Vivid Red
            sleep(STEP_DURATION);

            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            TreeNode successor = minValueNode(node.right);

            // Copy the inorder successor's content to this node
            node.value = successor.value;

            // 👇 FIX: Update the UI text of the current node with the new value
            updateNodeText(node.box, node.value);

            // Delete the inorder successor
            node.right = deleteNode(node.right, successor.value, new AtomicBoolean(true));
        }
        return node;
    }

    private TreeNode minValueNode(TreeNode node) {
        TreeNode current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    @FXML
    private void handleUpdate() {
        try {
            int oldValue = Integer.parseInt(inputField.getText());
            inputField.clear();

            // Ask for new value
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Node");
            dialog.setHeaderText("Update Node Value");
            dialog.setContentText("Enter new value:");
            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) return;

            int newValue = Integer.parseInt(result.get());

            new Thread(() -> {
                AtomicBoolean found = new AtomicBoolean(false);
                Platform.runLater(() -> showMessage("Updating " + oldValue + " → " + newValue + "..."));

                // Delete old value
                TreeNode newRoot = deleteNode(root, oldValue, found);

                if (!found.get()) {
                    Platform.runLater(() -> showMessage("Value " + oldValue + " not found!"));
                    return;
                }

                root = newRoot; // ✅ Make sure root updates correctly
                size--;

                // Insert new value
                root = insertNode(root, newValue);
                size++;

                Platform.runLater(() -> {
                    renderTree();
                    showMessage("Updated node " + oldValue + " to " + newValue + ".");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }


    @FXML
    private void handleFind() {
        try {
            int value = Integer.parseInt(inputField.getText());
            inputField.clear();
            new Thread(() -> {
                if (!findNode(root, value))
                    Platform.runLater(() -> showMessage("Value " + value + " not found!"));
            }).start();
        } catch (NumberFormatException e) {
            showMessage("Enter a valid number!");
        }
    }

    private boolean findNode(TreeNode node, int value) {
        if (node == null) return false;
        highlightNode(node.box, Color.web("#FFFF00")); // 💛 Bright Yellow
        sleep(STEP_DURATION);
        if (node.value == value) {
            Platform.runLater(() -> showMessage("Found value " + value + "."));
            highlightNode(node.box, Color.web("#00FF00")); // 💚 Neon Green
            return true;
        } else if (value < node.value) return findNode(node.left, value);
        else return findNode(node.right, value);
    }

    @FXML
    private void handleReset() {
        root = null;
        size = 0;
        treePane.getChildren().clear();
        sizeLabel.setText("Size: 0");
        heightLabel.setText("Height: 0");
        messageLabel.setText("");
        inputField.clear();
    }

    // ----------------- TRAVERSALS -----------------
    @FXML
    private void handlePreorder() { traverse(root, "preorder"); }
    @FXML
    private void handleInorder() { traverse(root, "inorder"); }
    @FXML
    private void handlePostorder() { traverse(root, "postorder"); }
    @FXML
    private void handleLevelOrder() { traverse(root, "levelorder"); }

    private void traverse(TreeNode node, String type) {
        new Thread(() -> {
            if (node == null) return;
            switch (type) {
                case "preorder" -> preorder(node);
                case "inorder" -> inorder(node);
                case "postorder" -> postorder(node);
                case "levelorder" -> levelOrder();
            }
            Platform.runLater(() ->
                    showMessage(type.substring(0,1).toUpperCase() + type.substring(1) + " traversal completed.")
            );
        }).start();
    }

    private void preorder(TreeNode node) {
        if (node == null) return;
        highlightNode(node.box, Color.web("#39FF14")); // 💚 Bright Neon Green
        sleep(STEP_DURATION);
        preorder(node.left);
        preorder(node.right);
    }

    private void inorder(TreeNode node) {
        if (node == null) return;
        inorder(node.left);
        highlightNode(node.box, Color.web("#00FFFF")); // 💙 Bright Cyan
        sleep(STEP_DURATION);
        inorder(node.right);
    }

    private void postorder(TreeNode node) {
        if (node == null) return;
        postorder(node.left);
        postorder(node.right);
        highlightNode(node.box, Color.web("#FF69B4")); // 💖 Hot Pink
        sleep(STEP_DURATION);
    }

    private void levelOrder() {
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            highlightNode(node.box, Color.web("#7CFC00")); // 💚 Lawn Green
            sleep(STEP_DURATION);
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
    }

    // ----------------- HELPER METHODS -----------------
    private void renderTree() {
        treePane.getChildren().clear();
        if (root != null)
            renderNode(root, treePane.getWidth() / 2, 50, treePane.getWidth() / 4);
        sizeLabel.setText("Size: " + size);
        heightLabel.setText("Height: " + treeHeight(root));
    }

    private void renderNode(TreeNode node, double x, double y, double offsetX) {
        if (node.left != null) {
            drawLine(x, y, x - offsetX, y + 80);
            renderNode(node.left, x - offsetX, y + 80, offsetX / 2);
        }
        if (node.right != null) {
            drawLine(x, y, x + offsetX, y + 80);
            renderNode(node.right, x + offsetX, y + 80, offsetX / 2);
        }
        VBox box = node.box;
        box.setLayoutX(x - 25);
        box.setLayoutY(y - 25);
        if (!treePane.getChildren().contains(box)) treePane.getChildren().add(box);
    }

    private VBox createNodeBox(int value) {
        Circle circle = new Circle(25, Color.web("#E6E6FA")); // 🎨 Soft lavender base
        circle.setStroke(Color.web("#4B0082")); // 💜 Indigo outline
        circle.setStrokeWidth(2);
        Text text = new Text(String.valueOf(value));
        text.setFill(Color.web("#000000"));
        text.setStyle("-fx-font-weight: bold;");
        VBox vbox = new VBox(circle, text);
        vbox.setSpacing(-15);
        vbox.setStyle("-fx-alignment: center;");
        return vbox;
    }

    /**
     * Updates the Text component within a node's VBox on the JavaFX thread.
     * This is the essential fix for the UI not updating during internal node deletion.
     */
    private void updateNodeText(VBox box, int newValue) {
        // Run on the JavaFX Application Thread since it updates the UI
        Platform.runLater(() -> {
            if (box != null && box.getChildren().size() > 1 && box.getChildren().get(1) instanceof Text text) {
                text.setText(String.valueOf(newValue));
            }
        });
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(Color.web("#5555FF")); // 💙 Light electric blue lines
        line.setStrokeWidth(2);
        treePane.getChildren().add(line);
    }

    private void highlightNode(VBox box, Color color) {
        Circle circle = (Circle) box.getChildren().get(0);
        FillTransition ft = new FillTransition(Duration.millis(STEP_DURATION), circle, (Color) circle.getFill(), color);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
        circle.setStroke(color);
        sleep(STEP_DURATION);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.web("#FF00FF")); // 💜 Magenta text for notifications
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

    private int treeHeight(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(treeHeight(node.left), treeHeight(node.right));
    }
}