package com.example.vlearn.controller.programming_basic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class IfElseVisualizerController {

    // --- Even/Odd ---
    @FXML private TextField evenOddInput;
    @FXML private TextFlow evenOddCodeFlow;
    @FXML private TextArea evenOddConsole;
    @FXML private FlowPane evenOddVisual;

    // --- Grade ---
    @FXML private TextField gradeInput;
    @FXML private TextFlow gradeCodeFlow;
    @FXML private TextArea gradeConsole;
    @FXML private FlowPane gradeVisual;

    // --- Leap Year ---
    @FXML private TextField leapYearInput;
    @FXML private TextFlow leapYearCodeFlow;
    @FXML private TextArea leapYearConsole;
    @FXML private FlowPane leapYearVisual;


    // --- Largest Number ---
    @FXML private TextField largestInput1, largestInput2, largestInput3;
    @FXML private TextFlow largestCodeFlow;
    @FXML private TextArea largestConsole;
    @FXML private FlowPane largestVisual;

    // --- Login ---
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private TextFlow loginCodeFlow;
    @FXML private TextArea loginConsole;
    @FXML private FlowPane loginVisual;

    // -------------------------------------------------------
    @FXML
    public void initialize() {
        // Update code when user types
        evenOddInput.textProperty().addListener((obs, oldVal, newVal) -> updateEvenOddCode());
        gradeInput.textProperty().addListener((obs, oldVal, newVal) -> updateGradeCode());
        leapYearInput.textProperty().addListener((obs, oldVal, newVal) -> updateLeapYearCode());
        largestInput1.textProperty().addListener((obs, oldVal, newVal) -> updateLargestCode());
        largestInput2.textProperty().addListener((obs, oldVal, newVal) -> updateLargestCode());
        largestInput3.textProperty().addListener((obs, oldVal, newVal) -> updateLargestCode());
        loginUsername.textProperty().addListener((obs, oldVal, newVal) -> updateLoginCode());
        loginPassword.textProperty().addListener((obs, oldVal, newVal) -> updateLoginCode());

        // Initialize code
        updateEvenOddCode();
        updateGradeCode();
        updateLeapYearCode();
        updateLargestCode();
        updateLoginCode();
    }

    // ---------------- Helpers ----------------
    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { e.printStackTrace(); }
    }

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

    private void highlightCodeLine(TextFlow codeFlow, int index) {
        Platform.runLater(() -> {
            for (int i = 0; i < codeFlow.getChildren().size(); i++) {
                Text t = (Text) codeFlow.getChildren().get(i);
                if (i == index) {
                    t.setFill(Color.ORANGE);
                    t.setStyle("-fx-font-weight: bold; -fx-background-color: yellow;");
                } else {
                    t.setFill(Color.WHITE);
                    t.setStyle("-fx-font-weight: normal; -fx-background-color: transparent;");
                }
            }
        });
    }

    // ----------------- Code Updates -----------------
    private void updateEvenOddCode() {
        int n; try { n = Integer.parseInt(evenOddInput.getText().trim()); } catch (Exception e) { n = 0; }
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int n = " + n + ";",
                "    if(n % 2 == 0) {",
                "        cout << \"Even\";",
                "    } else {",
                "        cout << \"Odd\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(evenOddCodeFlow, code);
    }

    private void updateGradeCode() {
        int marks; try { marks = Integer.parseInt(gradeInput.getText().trim()); } catch (Exception e) { marks = 0; }
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int marks = " + marks + ";",
                "    if(marks >= 80) cout << \"A\";",
                "    else if(marks >= 60) cout << \"B\";",
                "    else if(marks >= 40) cout << \"C\";",
                "    else cout << \"F\";",
                "    return 0;",
                "}"
        };
        showCode(gradeCodeFlow, code);
    }

    private void updateLeapYearCode() {
        int year; try { year = Integer.parseInt(leapYearInput.getText().trim()); } catch (Exception e) { year = 0; }
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int year = " + year + ";",
                "    if((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {",
                "        cout << \"Leap Year\";",
                "    } else {",
                "        cout << \"Not Leap Year\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(leapYearCodeFlow, code);
    }

    private void updateLargestCode() {
        int a, b, c;
        try { a = Integer.parseInt(largestInput1.getText().trim()); } catch (Exception e) { a = 0; }
        try { b = Integer.parseInt(largestInput2.getText().trim()); } catch (Exception e) { b = 0; }
        try { c = Integer.parseInt(largestInput3.getText().trim()); } catch (Exception e) { c = 0; }
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    int a = " + a + ";",
                "    int b = " + b + ";",
                "    int c = " + c + ";",
                "    int largest = max(a, max(b, c));",
                "    cout << largest;",
                "    return 0;",
                "}"
        };
        showCode(largestCodeFlow, code);
    }

    private void updateLoginCode() {
        String u = loginUsername.getText().trim();
        String p = loginPassword.getText().trim();
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int main() {",
                "    string username = \"" + u + "\";",
                "    string password = \"" + p + "\";",
                "    if(username == \"admin\" && password == \"1234\") {",
                "        cout << \"Login Success\";",
                "    } else {",
                "        cout << \"Login Failed\";",
                "    }",
                "    return 0;",
                "}"
        };
        showCode(loginCodeFlow, code);
    }

    // ----------------- Simulation Methods -----------------
    @FXML
    private void simulateEvenOdd() {
        new Thread(() -> {
            Platform.runLater(() -> { evenOddConsole.clear(); evenOddVisual.getChildren().clear(); });

            int n; try { n = Integer.parseInt(evenOddInput.getText().trim()); } catch (Exception e) { n = 0; }
            boolean even = n % 2 == 0;
            final int val = n;

            highlightCodeLine(evenOddCodeFlow, 4); sleep(500);
            highlightCodeLine(evenOddCodeFlow, 5); sleep(500);

            if (even) {
                highlightCodeLine(evenOddCodeFlow, 6);
                Platform.runLater(() -> {
                    evenOddConsole.appendText("Even\n");
                    evenOddVisual.getChildren().add(new Rectangle(60, 40, Color.GREEN));
                });
            } else {
                highlightCodeLine(evenOddCodeFlow, 8);
                Platform.runLater(() -> {
                    evenOddConsole.appendText("Odd\n");
                    evenOddVisual.getChildren().add(new Rectangle(60, 40, Color.RED));
                });
            }
            sleep(500);
            highlightCodeLine(evenOddCodeFlow, 10);
        }).start();
    }

    @FXML
    private void simulateGrade() {
        new Thread(() -> {
            Platform.runLater(() -> { gradeConsole.clear(); gradeVisual.getChildren().clear(); });

            int marks; try { marks = Integer.parseInt(gradeInput.getText().trim()); } catch (Exception e) { marks = 0; }
            String grade;
            if (marks >= 80) grade = "A";
            else if (marks >= 60) grade = "B";
            else if (marks >= 40) grade = "C";
            else grade = "F";
            final int val = marks;

            highlightCodeLine(gradeCodeFlow, 4); sleep(500);
            if (marks >= 80) highlightCodeLine(gradeCodeFlow, 5);
            else if (marks >= 60) highlightCodeLine(gradeCodeFlow, 6);
            else if (marks >= 40) highlightCodeLine(gradeCodeFlow, 7);
            else highlightCodeLine(gradeCodeFlow, 8);

            Color color = switch (grade) {
                case "A" -> Color.GREEN;
                case "B" -> Color.BLUE;
                case "C" -> Color.ORANGE;
                default -> Color.RED;
            };

            Platform.runLater(() -> {
                gradeConsole.appendText("Marks = " + val + "\nGrade = " + grade + "\n");
                gradeVisual.getChildren().add(new Rectangle(60, 40, color));
            });
            sleep(500);
            highlightCodeLine(gradeCodeFlow, 9);
        }).start();
    }

    @FXML
    private void simulateLeapYear() {
        new Thread(() -> {
            Platform.runLater(() -> { leapYearConsole.clear(); leapYearVisual.getChildren().clear(); });

            int year; try { year = Integer.parseInt(leapYearInput.getText().trim()); } catch (Exception e) { year = 0; }
            boolean isLeap = (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
            final int val = year;

            highlightCodeLine(leapYearCodeFlow, 4); sleep(500);
            highlightCodeLine(leapYearCodeFlow, isLeap ? 5 : 7);sleep(500);
            highlightCodeLine(leapYearCodeFlow, isLeap ? 6 : 8);

            Platform.runLater(() -> {
                leapYearConsole.appendText("Year = " + val + "\n");
                leapYearConsole.appendText(isLeap ? "Leap Year\n" : "Not a Leap Year\n");
                leapYearVisual.getChildren().add(new Rectangle(60, 40, isLeap ? Color.GREEN : Color.RED));
            });
            sleep(500);
            highlightCodeLine(leapYearCodeFlow, 10);
        }).start();
    }


    @FXML
    private void simulateLargest() {
        new Thread(() -> {
            Platform.runLater(() -> { largestConsole.clear(); largestVisual.getChildren().clear(); });

            final int a = parseIntSafe(largestInput1.getText());
            final int b = parseIntSafe(largestInput2.getText());
            final int c = parseIntSafe(largestInput3.getText());
            final int largest = Math.max(a, Math.max(b, c));

            highlightCodeLine(largestCodeFlow, 4); sleep(500);
            highlightCodeLine(largestCodeFlow, 5); sleep(500);
            highlightCodeLine(largestCodeFlow, 6); sleep(500);
            highlightCodeLine(largestCodeFlow, 7); sleep(500);
            highlightCodeLine(largestCodeFlow, 8); sleep(500);

            Platform.runLater(() -> {
                largestConsole.appendText("Numbers: " + a + ", " + b + ", " + c + "\nLargest: " + largest + "\n");
                largestVisual.getChildren().add(new Rectangle(60, 40, Color.BLUE));
            });
            highlightCodeLine(largestCodeFlow, 9);

        }).start();
    }

    // ---------------- Helper Methods ----------------
    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0; }
    }

    @FXML
    private void simulateLogin() {
        new Thread(() -> {
            Platform.runLater(() -> { loginConsole.clear(); loginVisual.getChildren().clear(); });

            String username = loginUsername.getText().trim();
            String password = loginPassword.getText().trim();
            boolean success = username.equals("admin") && password.equals("1234");

            highlightCodeLine(loginCodeFlow, 4); sleep(500);
            highlightCodeLine(loginCodeFlow, 5); sleep(500);
            highlightCodeLine(loginCodeFlow, 6); sleep(500);
            highlightCodeLine(loginCodeFlow, success ? 7 : 9); sleep(500);
            Platform.runLater(() -> {
                loginConsole.appendText("Username: " + username + "\nPassword: " + password + "\n");
                loginConsole.appendText(success ? "Login Success\n" : "Login Failed\n");
                loginVisual.getChildren().add(new Rectangle(60, 40, success ? Color.GREEN : Color.RED));
            });
            highlightCodeLine(loginCodeFlow, 11);

        }).start();
    }
}
