package com.example.vlearn.controller.programming_basic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SwitchVisualizerController {

    // -------------------- FXML Bindings --------------------
    @FXML private TextField numWordInput;
    @FXML private TextFlow numWordCodeFlow;
    @FXML private TextArea numWordConsole;
    @FXML private FlowPane numWordVisual;

    @FXML private TextField dayInput;
    @FXML private TextFlow dayCodeFlow;
    @FXML private TextArea dayConsole;
    @FXML private FlowPane dayVisual;

    @FXML private TextField menuInput;
    @FXML private TextFlow menuCodeFlow;
    @FXML private TextArea menuConsole;
    @FXML private FlowPane menuVisual;

    @FXML private TextField calcInput;
    @FXML private TextFlow calcCodeFlow;
    @FXML private TextArea calcConsole;
    @FXML private FlowPane calcVisual;

    @FXML private TextField trafficInput;
    @FXML private TextFlow trafficCodeFlow;
    @FXML private TextArea trafficConsole;
    @FXML private FlowPane trafficVisual;

    // -------------------- Initialize --------------------
    @FXML
    public void initialize() {
        numWordInput.textProperty().addListener((o, ov, nv) -> updateNumWordCode());
        dayInput.textProperty().addListener((o, ov, nv) -> updateDayCode());
        menuInput.textProperty().addListener((o, ov, nv) -> updateMenuCode());
        calcInput.textProperty().addListener((o, ov, nv) -> updateCalcCode());
        trafficInput.textProperty().addListener((o, ov, nv) -> updateTrafficCode());

        updateNumWordCode();
        updateDayCode();
        updateMenuCode();
        updateCalcCode();
        updateTrafficCode();
    }

    // -------------------- Helpers --------------------
    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private void showCode(TextFlow flow, String[] lines) {
        Platform.runLater(() -> {
            flow.getChildren().clear();
            for (String line : lines) {
                Text t = new Text(line + "\n");
                t.setFill(Color.web("#2C3E50FF"));
                t.setStyle("-fx-font-family: Consolas; -fx-font-size: 14;");
                flow.getChildren().add(t);
            }
        });
    }

    private void highlightLine(TextFlow flow, int index) {
        Platform.runLater(() -> {
            for (int i = 0; i < flow.getChildren().size(); i++) {
                Text t = (Text) flow.getChildren().get(i);
                if (i == index) {
                    t.setFill(Color.ORANGE);
                    t.setStyle("-fx-font-weight: bold; -fx-background-color: yellow;");
                } else {
                    t.setFill(Color.web("#2C3E50FF"));
                    t.setStyle("-fx-font-weight: normal; -fx-background-color: transparent;");
                }
            }
        });
    }

    private int parseSafe(String text) {
        try { return Integer.parseInt(text.trim()); } catch (Exception e) { return 0; }
    }

    // -------------------- Code Update Methods --------------------
    private void updateNumWordCode() {
        int n = parseSafe(numWordInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int n = " + n + ";",
                "    switch(n) {",
                "        case 1:",
                "            cout << \"One\";",
                "            break;",
                "        case 2:",
                "            cout << \"Two\";",
                "            break;",
                "        case 3:",
                "            cout << \"Three\";",
                "            break;",
                "        case 4:",
                "            cout << \"Four\";",
                "            break;",
                "        case 5:",
                "            cout << \"Five\";",
                "            break;",
                "        default:",
                "            cout << \"Invalid\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(numWordCodeFlow, code);
    }

    private void updateDayCode() {
        int d = parseSafe(dayInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int d = " + d + ";",
                "    switch(d) {",
                "        case 1:",
                "            cout << \"Monday\";",
                "            break;",
                "        case 2:",
                "            cout << \"Tuesday\";",
                "            break;",
                "        case 3:",
                "            cout << \"Wednesday\";",
                "            break;",
                "        case 4:",
                "            cout << \"Thursday\";",
                "            break;",
                "        case 5:",
                "            cout << \"Friday\";",
                "            break;",
                "        case 6:",
                "            cout << \"Saturday\";",
                "            break;",
                "        case 7:",
                "            cout << \"Sunday\";",
                "            break;",
                "        default:",
                "            cout << \"Invalid\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(dayCodeFlow, code);
    }

    private void updateMenuCode() {
        int c = parseSafe(menuInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int choice = " + c + ";",
                "    switch(choice) {",
                "        case 1:",
                "            cout << \"Pizza\";",
                "            break;",
                "        case 2:",
                "            cout << \"Burger\";",
                "            break;",
                "        case 3:",
                "            cout << \"Pasta\";",
                "            break;",
                "        case 4:",
                "            cout << \"Salad\";",
                "            break;",
                "        default:",
                "            cout << \"Invalid Choice\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(menuCodeFlow, code);
    }

    private void updateCalcCode() {
        int op = parseSafe(calcInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int op = " + op + ";",
                "    switch(op) {",
                "        case 1:",
                "            cout << \"Addition\";",
                "            break;",
                "        case 2:",
                "            cout << \"Subtraction\";",
                "            break;",
                "        case 3:",
                "            cout << \"Multiplication\";",
                "            break;",
                "        case 4:",
                "            cout << \"Division\";",
                "            break;",
                "        default:",
                "            cout << \"Invalid Operation\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(calcCodeFlow, code);
    }

    private void updateTrafficCode() {
        int light = parseSafe(trafficInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int light = " + light + ";",
                "    switch(light) {",
                "        case 1:",
                "            cout << \"Red\";",
                "            break;",
                "        case 2:",
                "            cout << \"Yellow\";",
                "            break;",
                "        case 3:",
                "            cout << \"Green\";",
                "            break;",
                "        default:",
                "            cout << \"Invalid\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(trafficCodeFlow, code);
    }

    // -------------------- Simulation Methods --------------------
// ============================================================
// Step-by-step Simulation Methods
// ============================================================

    @FXML
    private void simulateNumWord() {
        new Thread(() -> {
            Platform.runLater(() -> { numWordConsole.clear(); numWordVisual.getChildren().clear(); });

            int n = parseSafe(numWordInput.getText());
            String word = switch (n) {
                case 1 -> "One";
                case 2 -> "Two";
                case 3 -> "Three";
                case 4 -> "Four";
                case 5 -> "Five";
                default -> "Invalid";
            };

            highlightLine(numWordCodeFlow, 4); sleep(400);
            highlightLine(numWordCodeFlow, 5); sleep(500);

            int firstCaseIndex = 6;
            int casesCount = 5;

            if (n >= 1 && n <= casesCount) {
                for (int k = 1; k <= n; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    int coutLine = caseLine + 1;
                    int breakLine = caseLine + 2;

                    highlightLine(numWordCodeFlow, caseLine); sleep(400);
                    if(k==n){
                        highlightLine(numWordCodeFlow, coutLine); sleep(500);
                        highlightLine(numWordCodeFlow, breakLine); sleep(400);
                    }

                }
            } else {
                for (int k = 1; k <= casesCount; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    highlightLine(numWordCodeFlow, caseLine); sleep(250);

                }
                highlightLine(numWordCodeFlow, 21); sleep(350);
                highlightLine(numWordCodeFlow, 22); sleep(400);
            }

            Platform.runLater(() -> {
                numWordConsole.appendText("Input = " + n + "\nOutput = " + word + "\n");
                numWordVisual.getChildren().add(new Rectangle(80, 40, word.equals("Invalid") ? Color.RED : Color.GREEN));
            });
            sleep(350);
            highlightLine(numWordCodeFlow, 24);
        }).start();
    }

    @FXML
    private void simulateDay() {
        new Thread(() -> {
            Platform.runLater(() -> { dayConsole.clear(); dayVisual.getChildren().clear(); });

            int d = parseSafe(dayInput.getText());
            String day = switch (d) {
                case 1 -> "Monday";
                case 2 -> "Tuesday";
                case 3 -> "Wednesday";
                case 4 -> "Thursday";
                case 5 -> "Friday";
                case 6 -> "Saturday";
                case 7 -> "Sunday";
                default -> "Invalid";
            };

            highlightLine(dayCodeFlow, 4); sleep(400);
            highlightLine(dayCodeFlow, 5); sleep(500);

            int firstCaseIndex = 6;
            int casesCount = 7;

            if (d >= 1 && d <= casesCount) {
                for (int k = 1; k <= d; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    int coutLine = caseLine + 1;
                    int breakLine = caseLine + 2;

                    highlightLine(dayCodeFlow, caseLine); sleep(400);
                    if(k==d){
                        highlightLine(dayCodeFlow, coutLine); sleep(500);
                        highlightLine(dayCodeFlow, breakLine); sleep(400);
                    }

                }
            } else {
                for (int k = 1; k <= casesCount; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    highlightLine(dayCodeFlow, caseLine); sleep(250);
                }
                highlightLine(dayCodeFlow, 27); sleep(350);
                highlightLine(dayCodeFlow, 28); sleep(400);
            }

            Platform.runLater(() -> {
                dayConsole.appendText("Input = " + d + "\nOutput = " + day + "\n");
                dayVisual.getChildren().add(new Rectangle(100, 40, day.equals("Invalid") ? Color.RED : Color.GREEN));
            });
            sleep(350);
            highlightLine(dayCodeFlow, 30);
        }).start();
    }

    @FXML
    private void simulateMenu() {
        new Thread(() -> {
            Platform.runLater(() -> { menuConsole.clear(); menuVisual.getChildren().clear(); });

            int choice = parseSafe(menuInput.getText());
            String result = switch (choice) {
                case 1 -> "Pizza";
                case 2 -> "Burger";
                case 3 -> "Pasta";
                case 4 -> "Salad";
                default -> "Invalid Choice";
            };

            highlightLine(menuCodeFlow, 4); sleep(400);
            highlightLine(menuCodeFlow, 5); sleep(500);

            int firstCaseIndex = 6;
            int casesCount = 4;

            if (choice >= 1 && choice <= casesCount) {
                for (int k = 1; k <= choice; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    int coutLine = caseLine + 1;
                    int breakLine = caseLine + 2;

                    highlightLine(menuCodeFlow, caseLine); sleep(400);
                    if(k == choice) {
                        highlightLine(menuCodeFlow, coutLine); sleep(500);
                        highlightLine(menuCodeFlow, breakLine); sleep(400);
                    }

                }
            } else {
                for (int k = 1; k <= casesCount; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    highlightLine(menuCodeFlow, caseLine); sleep(250);
                }
                highlightLine(menuCodeFlow, 18); sleep(350);
                highlightLine(menuCodeFlow, 19); sleep(400);
            }

            Platform.runLater(() -> {
                menuConsole.appendText("Choice = " + choice + "\nSelected = " + result + "\n");
                menuVisual.getChildren().add(new Rectangle(80, 40, result.contains("Invalid") ? Color.RED : Color.GREEN));
            });
            sleep(350);
            highlightLine(menuCodeFlow, 21);
        }).start();
    }

    @FXML
    private void simulateCalc() {
        new Thread(() -> {
            Platform.runLater(() -> { calcConsole.clear(); calcVisual.getChildren().clear(); });

            int op = parseSafe(calcInput.getText());
            String operation = switch (op) {
                case 1 -> "Addition";
                case 2 -> "Subtraction";
                case 3 -> "Multiplication";
                case 4 -> "Division";
                default -> "Invalid Operation";
            };

            highlightLine(calcCodeFlow, 4); sleep(400);
            highlightLine(calcCodeFlow, 5); sleep(500);

            int firstCaseIndex = 6;
            int casesCount = 4;

            if (op >= 1 && op <= casesCount) {
                for (int k = 1; k <= op; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    int coutLine = caseLine + 1;
                    int breakLine = caseLine + 2;

                    highlightLine(calcCodeFlow, caseLine); sleep(400);
                    if(k == op) {
                        highlightLine(calcCodeFlow, coutLine); sleep(500);
                        highlightLine(calcCodeFlow, breakLine); sleep(400);
                    }

                }
            } else {
                for (int k = 1; k <= casesCount; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    highlightLine(calcCodeFlow, caseLine); sleep(250);
                }
                highlightLine(calcCodeFlow, 18); sleep(350);
                highlightLine(calcCodeFlow, 19); sleep(400);
            }

            Platform.runLater(() -> {
                calcConsole.appendText("Operation = " + op + "\nResult = " + operation + "\n");
                calcVisual.getChildren().add(new Rectangle(100, 40, operation.contains("Invalid") ? Color.RED : Color.GREEN));
            });
            sleep(350);
            highlightLine(calcCodeFlow, 21);
        }).start();
    }

    @FXML
    private void simulateTraffic() {
        new Thread(() -> {
            Platform.runLater(() -> { trafficConsole.clear(); trafficVisual.getChildren().clear(); });

            int light = parseSafe(trafficInput.getText());
            String signal;
            Color color;

            switch (light) {
                case 1 -> { signal = "Red"; color = Color.RED; }
                case 2 -> { signal = "Yellow"; color = Color.YELLOW; }
                case 3 -> { signal = "Green"; color = Color.GREEN; }
                default -> { signal = "Invalid"; color = Color.GRAY; }
            }

            highlightLine(trafficCodeFlow, 4); sleep(400);
            highlightLine(trafficCodeFlow, 5); sleep(500);

            int firstCaseIndex = 6;
            int casesCount = 3;

            if (light >= 1 && light <= casesCount) {
                for (int k = 1; k <= light; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    int coutLine = caseLine + 1;
                    int breakLine = caseLine + 2;

                    highlightLine(trafficCodeFlow, caseLine); sleep(400);
                    if(k == light) {
                        highlightLine(trafficCodeFlow, coutLine); sleep(500);
                        highlightLine(trafficCodeFlow, breakLine); sleep(400);
                    }

                }
            } else {
                for (int k = 1; k <= casesCount; k++) {
                    int caseLine = firstCaseIndex + (k - 1) * 3;
                    highlightLine(trafficCodeFlow, caseLine); sleep(250);
                }
                highlightLine(trafficCodeFlow, 15); sleep(350);
                highlightLine(trafficCodeFlow, 16); sleep(400);
            }

            Platform.runLater(() -> {
                trafficConsole.appendText("Light = " + light + "\nSignal = " + signal + "\n");
                trafficVisual.getChildren().add(new Rectangle(100, 40, color));
            });
            sleep(350);
            highlightLine(trafficCodeFlow, 18);
        }).start();
    }

}
