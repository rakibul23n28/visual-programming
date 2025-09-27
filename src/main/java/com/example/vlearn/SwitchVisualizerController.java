package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SwitchVisualizerController {

    // --- 1. Number to Word ---
    @FXML private TextField numWordInput;
    @FXML private TextArea numWordCode, numWordConsole;
    @FXML private FlowPane numWordVisual;

    @FXML
    private void numToWordSwitch() {
        new Thread(() -> {
            Platform.runLater(() -> {
                numWordConsole.clear();
                numWordVisual.getChildren().clear();
            });

            int input;
            try { input = Integer.parseInt(numWordInput.getText().trim()); } catch(Exception e) { input = 0; }
            final int n = input;

            final String word;
            switch(n) {
                case 1 -> word = "One";
                case 2 -> word = "Two";
                case 3 -> word = "Three";
                case 4 -> word = "Four";
                case 5 -> word = "Five";
                default -> word = "Invalid";
            }

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int n = %d;
                    switch(n) {
                        case 1: cout << "One"; break;
                        case 2: cout << "Two"; break;
                        case 3: cout << "Three"; break;
                        case 4: cout << "Four"; break;
                        case 5: cout << "Five"; break;
                        default: cout << "Invalid";
                    }
                    return 0;
                }
                """, n);

            Platform.runLater(() -> {
                numWordCode.setText(code);
                numWordConsole.appendText("Input = " + n + "\nOutput = " + word + "\n");
                numWordVisual.getChildren().add(new Rectangle(80, 40, word.equals("Invalid") ? Color.RED : Color.GREEN));
            });
        }).start();
    }

    // --- 2. Day of Week ---
    @FXML private TextField dayInput;
    @FXML private TextArea dayCode, dayConsole;
    @FXML private FlowPane dayVisual;

    @FXML
    private void dayOfWeekSwitch() {
        new Thread(() -> {
            Platform.runLater(() -> {
                dayConsole.clear();
                dayVisual.getChildren().clear();
            });

            int input;
            try { input = Integer.parseInt(dayInput.getText().trim()); } catch(Exception e) { input = 0; }
            final int d = input;

            final String day;
            switch(d) {
                case 1 -> day = "Monday";
                case 2 -> day = "Tuesday";
                case 3 -> day = "Wednesday";
                case 4 -> day = "Thursday";
                case 5 -> day = "Friday";
                case 6 -> day = "Saturday";
                case 7 -> day = "Sunday";
                default -> day = "Invalid";
            }

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int d = %d;
                    switch(d) {
                        case 1: cout << "Monday"; break;
                        case 2: cout << "Tuesday"; break;
                        case 3: cout << "Wednesday"; break;
                        case 4: cout << "Thursday"; break;
                        case 5: cout << "Friday"; break;
                        case 6: cout << "Saturday"; break;
                        case 7: cout << "Sunday"; break;
                        default: cout << "Invalid";
                    }
                    return 0;
                }
                """, d);

            Platform.runLater(() -> {
                dayCode.setText(code);
                dayConsole.appendText("Input = " + d + "\nOutput = " + day + "\n");
                dayVisual.getChildren().add(new Rectangle(100, 40, day.equals("Invalid") ? Color.RED : Color.GREEN));
            });
        }).start();
    }

    // --- 3. Menu Choice ---
    @FXML private TextField menuInput;
    @FXML private TextArea menuCode, menuConsole;
    @FXML private FlowPane menuVisual;

    @FXML
    private void menuChoiceSwitch() {
        new Thread(() -> {
            Platform.runLater(() -> {
                menuConsole.clear();
                menuVisual.getChildren().clear();
            });

            int input;
            try { input = Integer.parseInt(menuInput.getText().trim()); } catch(Exception e) { input = 0; }
            final int choice = input;

            final String result;
            switch(choice) {
                case 1 -> result = "Pizza";
                case 2 -> result = "Burger";
                case 3 -> result = "Pasta";
                case 4 -> result = "Salad";
                default -> result = "Invalid Choice";
            }

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int choice = %d;
                    switch(choice) {
                        case 1: cout << "Pizza"; break;
                        case 2: cout << "Burger"; break;
                        case 3: cout << "Pasta"; break;
                        case 4: cout << "Salad"; break;
                        default: cout << "Invalid Choice";
                    }
                    return 0;
                }
                """, choice);

            Platform.runLater(() -> {
                menuCode.setText(code);
                menuConsole.appendText("Choice = " + choice + "\nSelected = " + result + "\n");
                menuVisual.getChildren().add(new Rectangle(80, 40, result.contains("Invalid") ? Color.RED : Color.GREEN));
            });
        }).start();
    }

    // --- 4. Calculator Operation ---
    @FXML private TextField calcInput;
    @FXML private TextArea calcCode, calcConsole;
    @FXML private FlowPane calcVisual;

    @FXML
    private void calculatorSwitch() {
        new Thread(() -> {
            Platform.runLater(() -> {
                calcConsole.clear();
                calcVisual.getChildren().clear();
            });

            int input;
            try { input = Integer.parseInt(calcInput.getText().trim()); } catch(Exception e) { input = 0; }
            final int op = input;

            final String operation;
            switch(op) {
                case 1 -> operation = "Addition";
                case 2 -> operation = "Subtraction";
                case 3 -> operation = "Multiplication";
                case 4 -> operation = "Division";
                default -> operation = "Invalid Operation";
            }

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int op = %d;
                    switch(op) {
                        case 1: cout << "Addition"; break;
                        case 2: cout << "Subtraction"; break;
                        case 3: cout << "Multiplication"; break;
                        case 4: cout << "Division"; break;
                        default: cout << "Invalid Operation";
                    }
                    return 0;
                }
                """, op);

            Platform.runLater(() -> {
                calcCode.setText(code);
                calcConsole.appendText("Operation = " + op + "\nResult = " + operation + "\n");
                calcVisual.getChildren().add(new Rectangle(100, 40, operation.contains("Invalid") ? Color.RED : Color.GREEN));
            });
        }).start();
    }

    // --- 5. Traffic Light ---
    @FXML private TextField trafficInput;
    @FXML private TextArea trafficCode, trafficConsole;
    @FXML private FlowPane trafficVisual;

    @FXML
    private void trafficSwitch() {
        new Thread(() -> {
            Platform.runLater(() -> {
                trafficConsole.clear();
                trafficVisual.getChildren().clear();
            });

            int input;
            try { input = Integer.parseInt(trafficInput.getText().trim()); } catch(Exception e) { input = 0; }
            final int light = input;

            final String signal;
            final Color color;
            switch(light) {
                case 1 -> { signal = "Red"; color = Color.RED; }
                case 2 -> { signal = "Yellow"; color = Color.YELLOW; }
                case 3 -> { signal = "Green"; color = Color.GREEN; }
                default -> { signal = "Invalid"; color = Color.GRAY; }
            }

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int light = %d;
                    switch(light) {
                        case 1: cout << "Red"; break;
                        case 2: cout << "Yellow"; break;
                        case 3: cout << "Green"; break;
                        default: cout << "Invalid";
                    }
                    return 0;
                }
                """, light);

            Platform.runLater(() -> {
                trafficCode.setText(code);
                trafficConsole.appendText("Light = " + light + "\nSignal = " + signal + "\n");
                trafficVisual.getChildren().add(new Rectangle(100, 40, color));
            });
        }).start();
    }
}
