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

import java.util.concurrent.atomic.AtomicInteger;

public class LinkedListVisualizerController {

    @FXML private FlowPane linkedListContainer;
    @FXML private TextField inputField;
    @FXML private TextField indexField;
    @FXML private Label messageLabel;
    @FXML private HBox indexBox;
    @FXML private Label sizeLabel;

    private Node head;
    private int size = 0;
    private final int STEP_DURATION = 500;

    // ----------------- NODE CLASS -----------------
    private static class Node {
        int value;
        Node next;
        VBox box; // Visual representation

        Node(int value) {
            this.value = value;
        }
    }

    // ----------------- BUTTON HANDLERS -----------------

    @FXML
    private void handleInsertHead() {
        try {
            int value = parseInput(inputField.getText());
            clearInputs();

            Node newNode = new Node(value);
            newNode.next = head;
            head = newNode;
            size++;

            renderLinkedList();
            highlightNode(newNode.box, Color.LIMEGREEN);
            showMessage("Inserted " + value + " at head.");
        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleInsertTail() {
        try {
            int value = parseInput(inputField.getText());
            clearInputs();

            Node newNode = new Node(value);
            if (head == null) {
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;

            renderLinkedList();
            highlightNode(newNode.box, Color.LIMEGREEN);
            showMessage("Inserted " + value + " at tail.");
        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleInsertAtIndex() {
        try {
            int value = parseInput(inputField.getText());
            int index = parseIndex(indexField.getText(), -1);

            if (index < 0 || index > size) {
                showMessage("Index out of bounds!");
                return;
            }

            clearInputs();

            new Thread(() -> {
                Platform.runLater(() -> showMessage("Preparing to insert " + value + " at index " + index + "..."));

                Node newNode = new Node(value);
                if (index == 0) {
                    newNode.next = head;
                    head = newNode;
                } else {
                    Node current = head;
                    for (int i = 0; i < index - 1; i++) {
                        final Node temp = current;
                        Platform.runLater(() -> highlightNode(temp.box, Color.LIGHTBLUE));
                        try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}
                        current = current.next;
                    }
                    newNode.next = current.next;
                    current.next = newNode;
                }
                size++;
                Platform.runLater(() -> {
                    renderLinkedList();
                    highlightNode(newNode.box, Color.LIMEGREEN);
                    showMessage("Inserted " + value + " at index " + index + ".");
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter valid numbers!");
        }
    }

    @FXML
    private void handleDeleteHead() {
        if (head == null) {
            showMessage("List is empty!");
            return;
        }
        Node deletedNode = head;
        head = head.next;
        size--;

        renderLinkedList();
        highlightNode(deletedNode.box, Color.RED);
        showMessage("Deleted head node with value " + deletedNode.value);
    }

    @FXML
    private void handleDeleteTail() {
        if (head == null) {
            showMessage("List is empty!");
            return;
        }
        if (head.next == null) {
            Node deletedNode = head;
            head = null;
            size--;
            renderLinkedList();
            highlightNode(deletedNode.box, Color.RED);
            showMessage("Deleted tail node with value " + deletedNode.value);
            return;
        }

        Node current = head;
        while (current.next.next != null) {
            current = current.next;
        }
        Node deletedNode = current.next;
        current.next = null;
        size--;

        renderLinkedList();
        highlightNode(deletedNode.box, Color.RED);
        showMessage("Deleted tail node with value " + deletedNode.value);
    }

    @FXML
    private void handleDeleteAtIndex() {
        try {
            int index = parseIndex(indexField.getText(), -1);
            if (index < 0 || index >= size) {
                showMessage("Index out of bounds!");
                return;
            }

            clearInputs();

            new Thread(() -> {
                Node deletedNode;
                if (index == 0) {
                    deletedNode = head;
                    head = head.next;
                } else {
                    Node current = head;
                    for (int i = 0; i < index - 1; i++) {
                        final Node temp = current;
                        Platform.runLater(() -> highlightNode(temp.box, Color.LIGHTBLUE));
                        try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}
                        current = current.next;
                    }
                    deletedNode = current.next;
                    current.next = deletedNode.next;
                }
                size--;

                Platform.runLater(() -> {
                    renderLinkedList();
                    highlightNode(deletedNode.box, Color.RED);
                    showMessage("Deleted node with value " + deletedNode.value + " at index " + index);
                });
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid index!");
        }
    }

    @FXML
    private void handleDeleteByValue() {
        try {
            int value = parseInput(inputField.getText());
            clearInputs();

            new Thread(() -> {
                Node current = head, prev = null;
                AtomicInteger index = new AtomicInteger(0);
                boolean found = false;

                while (current != null) {
                    final Node temp = current;
                    Platform.runLater(() -> highlightNode(temp.box, Color.ORANGE));
                    try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}

                    if (current.value == value) {
                        found = true;
                        if (prev == null) {
                            head = current.next;
                        } else {
                            prev.next = current.next;
                        }
                        size--;
                        Node deletedNode = current;
                        Platform.runLater(() -> {
                            renderLinkedList();
                            highlightNode(deletedNode.box, Color.RED);
                            showMessage("Deleted node with value " + value + " at index " + index.get());
                        });
                        break;
                    }
                    prev = current;
                    current = current.next;
                    index.getAndIncrement();
                }

                if (!found) {
                    Platform.runLater(() -> showMessage("Value " + value + " not found!"));
                }
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            int index = parseIndex(indexField.getText(), -1);
            int value = parseInput(inputField.getText());

            if (index < 0 || index >= size) {
                showMessage("Index out of bounds!");
                return;
            }

            clearInputs();

            new Thread(() -> {
                Node current = head;

                // Traverse to the node at the given index
                for (int i = 0; i < index; i++) {
                    final Node temp = current; // capture current for lambda
                    Platform.runLater(() -> highlightNode(temp.box, Color.LIGHTBLUE));

                    try {
                        Thread.sleep(STEP_DURATION);
                    } catch (InterruptedException ignored) {}

                    current = current.next;
                }

                final Node nodeToUpdate = current; // capture the node to update
                nodeToUpdate.value = value;

                Platform.runLater(() -> {
                    renderLinkedList();
                    highlightNode(nodeToUpdate.box, Color.GOLD);
                    showMessage("Updated node at index " + index + " to value " + value);
                });

            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter valid numbers!");
        }
    }

    @FXML
    private void handleFind() {
        try {
            int value = parseInput(inputField.getText());
            clearInputs();

            new Thread(() -> {
                Node current = head;
                int idx = 0;
                boolean found = false;

                while (current != null) {
                    final Node temp = current;
                    final int index = idx;
                    Platform.runLater(() -> highlightNode(temp.box, Color.ORANGE));
                    try { Thread.sleep(STEP_DURATION); } catch (InterruptedException ignored) {}

                    if (current.value == value) {
                        found = true;
                        Platform.runLater(() -> {
                            showMessage("Found value " + value + " at index " + index);
                            Platform.runLater(() -> highlightNode(temp.box, Color.YELLOW));

                        });
                        break;
                    }
                    current = current.next;
                    idx++;
                }

                if (!found) {
                    Platform.runLater(() -> showMessage("Value " + value + " not found!"));
                }
            }).start();

        } catch (NumberFormatException e) {
            showMessage("Enter a valid value!");
        }
    }

    @FXML
    private void handleReset() {
        head = null;
        size = 0;
        linkedListContainer.getChildren().clear();
        sizeLabel.setText("Size: 0");
        messageLabel.setText("");
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
                if (messageLabel.getText().equals(message)) messageLabel.setText("");
            });
        }).start();
    }

    private void renderLinkedList() {
        linkedListContainer.getChildren().clear();
        Node current = head;
        int idx = 0;

        while (current != null) {
            VBox nodeBox = createNodeBox(current.value, idx);

            // Mark head or tail
            if (idx == 0) {
                Label headLabel = new Label("HEAD");
                headLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                VBox wrapper = new VBox(headLabel, nodeBox);
                wrapper.setSpacing(5);
                wrapper.setStyle("-fx-alignment: center;");
                current.box = nodeBox;
                linkedListContainer.getChildren().add(wrapper);
            } else if (current.next == null) {
                Label tailLabel = new Label("TAIL");
                tailLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                VBox wrapper = new VBox(nodeBox, tailLabel);
                wrapper.setSpacing(5);
                wrapper.setStyle("-fx-alignment: center;");
                current.box = nodeBox;
                linkedListContainer.getChildren().add(wrapper);
            } else {
                current.box = nodeBox;
                linkedListContainer.getChildren().add(nodeBox);
            }

            // Add arrow if not last node
            if (current.next != null) {
                Text arrow = new Text("→");
                arrow.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                arrow.setFill(Color.DARKORANGE); // Set arrow color here
                linkedListContainer.getChildren().add(arrow);
            }

            current = current.next;
            idx++;
        }

        sizeLabel.setText("Size: " + size);
    }



    private VBox createNodeBox(int value, int index) {
        Rectangle rect = new Rectangle(60, 60);
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setFill(Color.web("#F0F8FF")); // You can choose a different fill if white text is hard to see
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);

        Text text = new Text(String.valueOf(value));
        text.setFill(Color.BLACK); // White text
        text.setStyle("-fx-font-weight: bold;"); // Bold text

        VBox vbox = new VBox(rect, text);
        vbox.setSpacing(0);
        vbox.setStyle("-fx-alignment: center;");
        Tooltip.install(vbox, new Tooltip("Index: " + index));
        return vbox;
    }



    private void highlightNode(VBox box, Color color) {
        Rectangle rect = (Rectangle) box.getChildren().get(0);
        rect.setStroke(color);
        rect.setStrokeWidth(4);

        FillTransition ft = new FillTransition(Duration.millis(STEP_DURATION), rect, (Color) rect.getFill(), color);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
    }
}
