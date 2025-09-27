package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LoopsVisualizerController {

    @FXML private TextField forInitField, forCondField, forIncField;
    @FXML private TextArea forCodeArea, forConsoleOutput;
    @FXML private Label forCurrentValue, forConditionStatus;
    @FXML private FlowPane forVisualOutput;

    @FXML private TextField whileInitField, whileCondField, whileIncField;
    @FXML private TextArea whileCodeArea, whileConsoleOutput;
    @FXML private Label whileCurrentValue, whileConditionStatus;
    @FXML private FlowPane whileVisualOutput;

    @FXML private TextField doWhileInitField, doWhileCondField, doWhileIncField;
    @FXML private TextArea doWhileCodeArea, doWhileConsoleOutput;
    @FXML private Label doWhileCurrentValue, doWhileConditionStatus;
    @FXML private FlowPane doWhileVisualOutput;

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    // --- FOR loop ---
    @FXML
    private void simulateForLoop() {
        new Thread(() -> {
            Platform.runLater(() -> {
                forVisualOutput.getChildren().clear();
                forConsoleOutput.clear();
                forCurrentValue.setText("...");
                forConditionStatus.setText("Checking...");
            });

            int init = parseInit(forInitField.getText());
            String condition = forCondField.getText().trim();
            String increment = forIncField.getText().trim();

            // Show generated C++ for-loop
            forCodeArea.setText(
                    "#include<iostream>\nusing namespace std;\n\nint main() {\n" +
                            "    for(int i = " + init + "; " + condition + "; " + increment + ") {\n" +
                            "        cout << \"Iteration: \" << i << \"\\n\";\n" +
                            "    }\n    return 0;\n}"
            );

            int i = init;
            final int initialFor = i;
            Platform.runLater(() -> forCurrentValue.setText(String.valueOf(initialFor)));

            while (evaluateCondition(i, condition)) {
                final int current = i;
                Platform.runLater(() -> {
                    forConditionStatus.setText("True");
                    forConsoleOutput.appendText("Iteration: " + current + "\n");
                    forVisualOutput.getChildren().add(new Rectangle(40, 40, Color.INDIGO));
                });

                sleep(1000);
                i = applyIncrement(i, increment);
                final int updated = i;
                Platform.runLater(() -> forCurrentValue.setText(String.valueOf(updated)));
            }

            Platform.runLater(() -> forConditionStatus.setText("False"));
        }).start();
    }

    // --- WHILE loop ---
    @FXML
    private void simulateWhileLoop() {
        new Thread(() -> {
            Platform.runLater(() -> {
                whileVisualOutput.getChildren().clear();
                whileConsoleOutput.clear();
                whileCurrentValue.setText("...");
                whileConditionStatus.setText("Checking...");
            });

            String condition = whileCondField.getText().trim();
            int init = parseInit(whileInitField.getText());
            String increment = whileIncField.getText().trim();

            // Show generated C++ while-loop
            whileCodeArea.setText(
                    "#include<iostream>\nusing namespace std;\n\nint main() {\n" +
                            "    int i = " + init + ";\n" +
                            "    while(" + condition + ") {\n" +
                            "        cout << \"Iteration: \" << i << \"\\n\";\n" +
                            "        " + increment + ";\n" +
                            "    }\n    return 0;\n}"
            );

            int i = init;
            final int initialWhile = i;
            Platform.runLater(() -> whileCurrentValue.setText(String.valueOf(initialWhile)));

            while (evaluateCondition(i, condition)) {
                final int current = i;
                Platform.runLater(() -> {
                    whileConditionStatus.setText("True");
                    whileConsoleOutput.appendText("Iteration: " + current + "\n");
                    whileVisualOutput.getChildren().add(new Rectangle(40, 40, Color.INDIGO));
                });

                sleep(1000);
                i = applyIncrement(i, increment.isEmpty() ? "i++" : increment);
                final int updated = i;
                Platform.runLater(() -> whileCurrentValue.setText(String.valueOf(updated)));
            }

            Platform.runLater(() -> whileConditionStatus.setText("False"));
        }).start();
    }

    // --- DO-WHILE loop ---
    @FXML
    private void simulateDoWhileLoop() {
        new Thread(() -> {
            Platform.runLater(() -> {
                doWhileVisualOutput.getChildren().clear();
                doWhileConsoleOutput.clear();
                doWhileCurrentValue.setText("...");
                doWhileConditionStatus.setText("Checking...");
            });

            int init = parseInit(doWhileInitField.getText());
            String condition = doWhileCondField.getText().trim();
            String increment = doWhileIncField.getText().trim();

            // Show generated C++ do-while loop
            doWhileCodeArea.setText(
                    "#include<iostream>\nusing namespace std;\n\nint main() {\n" +
                            "    int i = " + init + ";\n" +
                            "    do {\n" +
                            "        cout << \"Iteration: \" << i << \"\\n\";\n" +
                            "        " + (increment.isEmpty() ? "i++" : increment) + ";\n" +
                            "    } while(" + condition + ");\n" +
                            "    return 0;\n}"
            );

            int i = init;
            do {
                final int current = i;
                Platform.runLater(() -> {
                    doWhileConditionStatus.setText("True");
                    doWhileConsoleOutput.appendText("Iteration: " + current + "\n");
                    doWhileVisualOutput.getChildren().add(new Rectangle(40, 40, Color.INDIGO));
                });

                sleep(1000);
                i = applyIncrement(i, increment.isEmpty() ? "i++" : increment);
                final int updated = i;
                Platform.runLater(() -> doWhileCurrentValue.setText(String.valueOf(updated)));
            } while (evaluateCondition(i, condition));

            Platform.runLater(() -> doWhileConditionStatus.setText("False"));
        }).start();
    }

    // --- Helper methods ---
    private int parseInit(String text) {
        try {
            String[] parts = text.split("=");
            return Integer.parseInt(parts[1].trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean evaluateCondition(int i, String cond) {
        cond = cond.replaceAll("i", String.valueOf(i));
        if (cond.contains("<=")) return i <= Integer.parseInt(cond.split("<=")[1].trim());
        if (cond.contains("<")) return i < Integer.parseInt(cond.split("<")[1].trim());
        if (cond.contains(">=")) return i >= Integer.parseInt(cond.split(">=")[1].trim());
        if (cond.contains(">")) return i > Integer.parseInt(cond.split(">")[1].trim());
        if (cond.contains("==")) return i == Integer.parseInt(cond.split("==")[1].trim());
        return false;
    }

    private int applyIncrement(int i, String inc) {
        if (inc.equals("i++")) return i + 1;
        if (inc.equals("i--")) return i - 1;
        if (inc.contains("+=")) return i + Integer.parseInt(inc.split("\\+=")[1].trim());
        if (inc.contains("-=")) return i - Integer.parseInt(inc.split("-=")[1].trim());
        return i;
    }
}
