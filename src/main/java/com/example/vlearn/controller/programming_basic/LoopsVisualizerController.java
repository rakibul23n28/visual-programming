package com.example.vlearn.controller.programming_basic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LoopsVisualizerController {

    // --- FOR loop ---
    @FXML private TextField forInitField, forCondField, forIncField;
    @FXML private TextFlow forCodeFlow;
    @FXML private TextArea forConsoleOutput;
    @FXML private Label forCurrentValue, forConditionStatus;
    @FXML private FlowPane forVisualOutput;

    // --- WHILE loop ---
    @FXML private TextField whileInitField, whileCondField, whileIncField;
    @FXML private TextFlow whileCodeFlow;
    @FXML private TextArea whileConsoleOutput;
    @FXML private Label whileCurrentValue, whileConditionStatus;
    @FXML private FlowPane whileVisualOutput;

    // --- DO-WHILE loop ---
    @FXML private TextField doWhileInitField, doWhileCondField, doWhileIncField;
    @FXML private TextFlow doWhileCodeFlow;
    @FXML private TextArea doWhileConsoleOutput;
    @FXML private Label doWhileCurrentValue, doWhileConditionStatus;
    @FXML private FlowPane doWhileVisualOutput;

    // ---------------------------------------------------------------------

    @FXML
    public void initialize() {
        // FOR loop fields
        forInitField.textProperty().addListener((obs, oldVal, newVal) -> updateForCode());
        forCondField.textProperty().addListener((obs, oldVal, newVal) -> updateForCode());
        forIncField.textProperty().addListener((obs, oldVal, newVal) -> updateForCode());

        // WHILE loop fields
        whileInitField.textProperty().addListener((obs, oldVal, newVal) -> updateWhileCode());
        whileCondField.textProperty().addListener((obs, oldVal, newVal) -> updateWhileCode());
        whileIncField.textProperty().addListener((obs, oldVal, newVal) -> updateWhileCode());

        // DO-WHILE loop fields
        doWhileInitField.textProperty().addListener((obs, oldVal, newVal) -> updateDoWhileCode());
        doWhileCondField.textProperty().addListener((obs, oldVal, newVal) -> updateDoWhileCode());
        doWhileIncField.textProperty().addListener((obs, oldVal, newVal) -> updateDoWhileCode());

        // Initialize code display
        updateForCode();
        updateWhileCode();
        updateDoWhileCode();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    /** Highlights a specific line number inside a TextFlow (one at a time). */
    private void highlightCodeLine(TextFlow codeFlow, int lineIndex) {
        Platform.runLater(() -> {
            for (int i = 0; i < codeFlow.getChildren().size(); i++) {
                Text line = (Text) codeFlow.getChildren().get(i);
                if (i == lineIndex) {
                    line.setFill(Color.ORANGE);
                    line.setStyle("-fx-font-weight: bold; -fx-background-color: yellow;");
                } else {
                    line.setFill(Color.WHITE);
                    line.setStyle("-fx-font-weight: normal; -fx-background-color: transparent;");
                }
            }
        });
    }

    /** Loads lines of C++ code into a TextFlow */
    private void showCode(TextFlow codeFlow, String[] lines) {
        Platform.runLater(() -> {
            codeFlow.getChildren().clear();
            for (String line : lines) {
                Text t = new Text(line + "\n");
                t.setFill(Color.WHITE);
                t.setStyle("-fx-font-family: Consolas; -fx-font-size: 14;");
                codeFlow.getChildren().add(t);
            }
        });
    }

    // ---------------------------------------------------------------------
    // --- UPDATE CODE METHODS (LIVE) ---
    // ---------------------------------------------------------------------
    private void updateForCode() {
        int init = parseInit(forInitField.getText());
        String condition = forCondField.getText().trim();
        String increment = forIncField.getText().trim();

        String[] code = {
                "#include<iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    for(int i = " + init + "; " + condition + "; " + increment + ") {",
                "        cout << \"Iteration: \" << i << \"\\n\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(forCodeFlow, code);
    }

    private void updateWhileCode() {
        int init = parseInit(whileInitField.getText());
        String condition = whileCondField.getText().trim();
        String increment = whileIncField.getText().trim().isEmpty() ? "i++" : whileIncField.getText().trim();

        String[] code = {
                "#include<iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int i = " + init + ";",
                "    while(" + condition + ") {",
                "        cout << \"Iteration: \" << i << \"\\n\";",
                "        " + increment + ";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(whileCodeFlow, code);
    }

    private void updateDoWhileCode() {
        int init = parseInit(doWhileInitField.getText());
        String condition = doWhileCondField.getText().trim();
        String increment = doWhileIncField.getText().trim().isEmpty() ? "i++" : doWhileIncField.getText().trim();

        String[] code = {
                "#include<iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int i = " + init + ";",
                "    do {",
                "        cout << \"Iteration: \" << i << \"\\n\";",
                "        " + increment + ";",
                "    } while(" + condition + ");",
                "    return 0;",
                "}"
        };
        showCode(doWhileCodeFlow, code);
    }

    // ---------------------------------------------------------------------
    // --- SIMULATION METHODS ---
    // ---------------------------------------------------------------------

    @FXML
    private void simulateWhileLoop() {
        new Thread(() -> {
            Platform.runLater(() -> {
                whileVisualOutput.getChildren().clear();
                whileConsoleOutput.clear();
                whileCurrentValue.setText("...");
                whileConditionStatus.setText("Checking...");
            });

            int i = parseInit(whileInitField.getText());
            String condition = whileCondField.getText().trim();
            String increment = whileIncField.getText().trim().isEmpty() ? "i++" : whileIncField.getText().trim();

            while (true) {
                // Highlight the while loop line (condition check)
                highlightCodeLine(whileCodeFlow, 5);


                boolean condResult = evaluateCondition(i, condition);
                final int current = i;  // final copy for lambda

                Platform.runLater(() -> {
                    whileCurrentValue.setText(String.valueOf(current));
                    whileConditionStatus.setText(condResult ? "True" : "False");
                    whileConditionStatus.setTextFill(condResult ? Color.LIMEGREEN : Color.RED);
                });
                sleep(1000);

                if (!condResult) break;

                // Highlight cout line inside while
                highlightCodeLine(whileCodeFlow, 6);


                Platform.runLater(() -> {
                    whileConsoleOutput.appendText("Iteration: " + current + "\n");
                    whileVisualOutput.getChildren().add(new Rectangle(40, 40, Color.INDIGO));
                    whileCurrentValue.setText(String.valueOf(current));
                });
                sleep(1000);

                // Highlight increment line
                highlightCodeLine(whileCodeFlow, 7);

                i = applyIncrement(i, increment);
                final int updated = i; // final copy for lambda
                Platform.runLater(() -> whileCurrentValue.setText(String.valueOf(updated)));
                sleep(800);
            }

            // Highlight return line
            highlightCodeLine(whileCodeFlow, 9);
            Platform.runLater(() -> whileConditionStatus.setText("False"));
        }).start();
    }

    @FXML
    private void simulateForLoop() {
        new Thread(() -> {
            Platform.runLater(() -> {
                forVisualOutput.getChildren().clear();
                forConsoleOutput.clear();
                forCurrentValue.setText("...");
                forConditionStatus.setText("Checking...");
            });

            int i = parseInit(forInitField.getText());
            String condition = forCondField.getText().trim();
            String increment = forIncField.getText().trim().isEmpty() ? "i++" : forIncField.getText().trim();

            while (true) {
                // Highlight the for loop line (condition check)
                highlightCodeLine(forCodeFlow, 4);

                boolean condResult = evaluateCondition(i, condition);
                final int current = i;

                Platform.runLater(() -> {
                    forCurrentValue.setText(String.valueOf(current));
                    forConditionStatus.setText(condResult ? "True" : "False");
                    forConditionStatus.setTextFill(condResult ? Color.LIMEGREEN : Color.RED);
                });
                sleep(800);

                if (!condResult) break;

                // Highlight cout line
                highlightCodeLine(forCodeFlow, 5);
                Platform.runLater(() -> {
                    forConsoleOutput.appendText("Iteration: " + current + "\n");
                    forVisualOutput.getChildren().add(new Rectangle(40, 40, Color.INDIGO));
                });
                sleep(800);

                // Highlight increment
                highlightCodeLine(forCodeFlow, 4); // back to for line
                i = applyIncrement(i, increment);
                final int updated = i;
                Platform.runLater(() -> forCurrentValue.setText(String.valueOf(updated)));
                sleep(800);
            }

            // Highlight return line
            highlightCodeLine(forCodeFlow, 7);
            Platform.runLater(() -> forConditionStatus.setText("False"));
        }).start();
    }

    @FXML
    private void simulateDoWhileLoop() {
        new Thread(() -> {
            Platform.runLater(() -> {
                doWhileVisualOutput.getChildren().clear();
                doWhileConsoleOutput.clear();
                doWhileCurrentValue.setText("...");
                doWhileConditionStatus.setText("Checking...");
            });

            int i = parseInit(doWhileInitField.getText());
            String condition = doWhileCondField.getText().trim();
            String increment = doWhileIncField.getText().trim().isEmpty() ? "i++" : doWhileIncField.getText().trim();

            boolean firstIteration = true;

            do {
                final int current = i;

                // Highlight the 'do {' line
                highlightCodeLine(doWhileCodeFlow, 5);
                sleep(100);
                highlightCodeLine(doWhileCodeFlow, 6);

                // Display iteration output
                Platform.runLater(() -> {
                    doWhileConsoleOutput.appendText("Iteration: " + current + "\n");
                    doWhileVisualOutput.getChildren().add(new Rectangle(40, 40, Color.INDIGO));
                    doWhileCurrentValue.setText(String.valueOf(current));
                    doWhileConditionStatus.setText("True");
                    doWhileConditionStatus.setTextFill(Color.LIMEGREEN);
                });
                sleep(1000);

                // Highlight increment line
                highlightCodeLine(doWhileCodeFlow, 7);
                i = applyIncrement(i, increment);
                final int updated = i;
                Platform.runLater(() -> doWhileCurrentValue.setText(String.valueOf(updated)));
                sleep(800);

                // Highlight while(condition) line
                highlightCodeLine(doWhileCodeFlow, 8);
                boolean condResult = evaluateCondition(i, condition);
                Platform.runLater(() -> {
                    doWhileConditionStatus.setText(condResult ? "True" : "False");
                    doWhileConditionStatus.setTextFill(condResult ? Color.LIMEGREEN : Color.RED);
                });
                sleep(800);

                firstIteration = false;

            } while (evaluateCondition(i, condition) || firstIteration);

            // Highlight return line
            highlightCodeLine(doWhileCodeFlow, 9);
            Platform.runLater(() -> {
                doWhileConditionStatus.setText("False");
                doWhileConditionStatus.setTextFill(Color.RED);
            });
        }).start();
    }


    // ---------------------------------------------------------------------
    // --- HELPERS ---
    // ---------------------------------------------------------------------
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
        try {
            if (cond.contains("<=")) return i <= Integer.parseInt(cond.split("<=")[1].trim());
            if (cond.contains("<")) return i < Integer.parseInt(cond.split("<")[1].trim());
            if (cond.contains(">=")) return i >= Integer.parseInt(cond.split(">=")[1].trim());
            if (cond.contains(">")) return i > Integer.parseInt(cond.split(">")[1].trim());
            if (cond.contains("==")) return i == Integer.parseInt(cond.split("==")[1].trim());
        } catch (Exception ignored) {}
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
