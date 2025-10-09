package com.example.vlearn.controller.programming_basic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FunctionVisualizerController {

    // --- Addition ---
    @FXML private TextField addInput1, addInput2;
    @FXML private TextFlow addCodeFlow;
    @FXML private TextArea addConsole;
    @FXML private FlowPane addVisual;

    // --- Factorial ---
    @FXML private TextField factInput;
    @FXML private TextFlow factCodeFlow;
    @FXML private TextArea factConsole;
    @FXML private FlowPane factVisual;

    // --- Palindrome ---
    @FXML private TextField palInput;
    @FXML private TextFlow palCodeFlow;
    @FXML private TextArea palConsole;
    @FXML private FlowPane palVisual;

    // --- Fibonacci ---
    @FXML private TextField fibInput;
    @FXML private TextFlow fibCodeFlow;
    @FXML private TextArea fibConsole;
    @FXML private FlowPane fibVisual;

    // --- Prime ---
    @FXML private TextField primeInput;
    @FXML private TextFlow primeCodeFlow;
    @FXML private TextArea primeConsole;
    @FXML private FlowPane primeVisual;

    // ---------------- Initialization ----------------
    @FXML
    public void initialize() {
        // Update code on input change
        addInput1.textProperty().addListener((obs,o,n)->updateAddCode());
        addInput2.textProperty().addListener((obs,o,n)->updateAddCode());
        factInput.textProperty().addListener((obs,o,n)->updateFactCode());
        palInput.textProperty().addListener((obs,o,n)->updatePalCode());
        fibInput.textProperty().addListener((obs,o,n)->updateFibCode());
        primeInput.textProperty().addListener((obs,o,n)->updatePrimeCode());

        updateAddCode();
        updateFactCode();
        updatePalCode();
        updateFibCode();
        updatePrimeCode();
    }

    // ---------------- Helpers ----------------
    private void sleep(int ms) { try { Thread.sleep(ms); } catch(Exception e){ e.printStackTrace(); } }

    private void showCode(TextFlow codeFlow, String[] lines) {
        Platform.runLater(() -> {
            codeFlow.getChildren().clear();
            for(String line: lines) {
                Text t = new Text(line + "\n");
                t.setFill(Color.WHITE);
                t.setStyle("-fx-font-family: Consolas; -fx-font-size: 14;");
                codeFlow.getChildren().add(t);
            }
        });
    }

    private void highlightCodeLine(TextFlow codeFlow, int index) {
        Platform.runLater(() -> {
            for(int i=0;i<codeFlow.getChildren().size();i++) {
                Text t = (Text) codeFlow.getChildren().get(i);
                if(i==index){
                    t.setFill(Color.ORANGE);
                    t.setStyle("-fx-font-weight:bold;-fx-background-color:yellow;");
                } else {
                    t.setFill(Color.WHITE);
                    t.setStyle("-fx-font-weight:normal;-fx-background-color:transparent;");
                }
            }
        });
    }

    private int parseIntSafe(String s) { try { return Integer.parseInt(s.trim()); } catch(Exception e){ return 0; } }

    // ---------------- Update Code ----------------
    private void updateAddCode() {
        int a = parseIntSafe(addInput1.getText());
        int b = parseIntSafe(addInput2.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int add(int x, int y) {",
                "    return x + y;",
                "}",
                "",
                "int main() {",
                "    int a = " + a + ", b = " + b + ";",
                "    cout << add(a,b);",
                "    return 0;",
                "}"
        };
        showCode(addCodeFlow, code);
    }

    private void updateFactCode() {
        int n = parseIntSafe(factInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "int factorial(int n) {",
                "    int result = 1;",
                "    for(int i=1;i<=n;i++) result*=i;",
                "    return result;",
                "}",
                "",
                "int main() {",
                "    int n = " + n + ";",
                "    cout << factorial(n);",
                "    return 0;",
                "}"
        };
        showCode(factCodeFlow, code);
    }

    private void updatePalCode() {
        String text = palInput.getText().trim();
        String[] code = {
                "#include <iostream>",
                "#include <string>",
                "using namespace std;",
                "",
                "bool isPalindrome(string s) {",
                "    string rev = string(s.rbegin(), s.rend());",
                "    return s == rev;",
                "}",
                "",
                "int main() {",
                "    string text = \"" + text + "\";",
                "    cout << (isPalindrome(text)?\"Palindrome\":\"Not Palindrome\");",
                "    return 0;",
                "}"
        };
        showCode(palCodeFlow, code);
    }

    private void updateFibCode() {
        int n = parseIntSafe(fibInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "void fibonacci(int n) {",
                "    int a=0, b=1;",
                "    for(int i=0;i<n;i++) {",
                "        cout << a << \" \";",
                "        int c = a + b;",
                "        a = b;",
                "        b = c;",
                "    }",
                "}",
                "",
                "int main() {",
                "    int n = " + n + ";",
                "    fibonacci(n);",
                "    return 0;",
                "}"
        };
        showCode(fibCodeFlow, code);
    }

    private void updatePrimeCode() {
        int n = parseIntSafe(primeInput.getText());
        String[] code = {
                "#include <iostream>",
                "using namespace std;",
                "",
                "bool isPrime(int n) {",
                "    if(n<=1) return false;",
                "    for(int i=2;i*i<=n;i++)",
                "        if(n%i==0) return false;",
                "    return true;",
                "}",
                "",
                "int main() {",
                "    int n = " + n + ";",
                "    cout << (isPrime(n)?\"Prime\":\"Not Prime\");",
                "    return 0;",
                "}"
        };
        showCode(primeCodeFlow, code);
    }

    // ---------------- Simulation ----------------
    @FXML
    private void simulateAddition() {
        new Thread(() -> {
            Platform.runLater(() -> { addConsole.clear(); addVisual.getChildren().clear(); });
            int a = parseIntSafe(addInput1.getText());
            int b = parseIntSafe(addInput2.getText());
            int sum = a + b;
            highlightCodeLine(addCodeFlow, 7); sleep(500); // int a,b
            highlightCodeLine(addCodeFlow, 8); sleep(500); // int a,b
            highlightCodeLine(addCodeFlow, 9); sleep(500); // int a,b
            highlightCodeLine(addCodeFlow, 3); sleep(500); // return x+y;
            highlightCodeLine(addCodeFlow, 4); sleep(500); // return x+y;
            highlightCodeLine(addCodeFlow, 9); sleep(500); // cout << add(a,b);

            Platform.runLater(() -> {
                addConsole.appendText(a + " + " + b + " = " + sum + "\n");
                addVisual.getChildren().add(new Rectangle(80,40,Color.GREEN));
            });
            sleep(500);
            highlightCodeLine(addCodeFlow, 10); // return 0;
        }).start();
    }

    @FXML
    private void simulateFactorial() {
        new Thread(() -> {
            Platform.runLater(() -> { factConsole.clear(); factVisual.getChildren().clear(); });
            int n = parseIntSafe(factInput.getText());
            int result = 1;
            for(int i=1;i<=n;i++) result*=i;
            highlightCodeLine(factCodeFlow,9); sleep(500); // result=1
            highlightCodeLine(factCodeFlow,10); sleep(500); // result=1
            highlightCodeLine(factCodeFlow,11); sleep(500); // result=1

            highlightCodeLine(factCodeFlow,3); sleep(500); // result=1
            highlightCodeLine(factCodeFlow,4); sleep(500); // result=1
            highlightCodeLine(factCodeFlow,5); sleep(500); // for loop
            highlightCodeLine(factCodeFlow,6); sleep(500); // result*=i
            highlightCodeLine(factCodeFlow,11); sleep(500); // return result

            int factorial = result;
            Platform.runLater(() -> {
                factConsole.appendText("Factorial of " + n + " = " + factorial + "\n");
                factVisual.getChildren().add(new Rectangle(80,40,Color.ORANGE));
            });
            sleep(500);
            highlightCodeLine(factCodeFlow,12); // return 0
        }).start();
    }

    @FXML
    private void simulatePalindrome() {
        new Thread(() -> {
            Platform.runLater(() -> { palConsole.clear(); palVisual.getChildren().clear(); });
            String text = palInput.getText().trim();
            boolean isPalindrome = text.equalsIgnoreCase(new StringBuilder(text).reverse().toString());

            highlightCodeLine(palCodeFlow,9); sleep(500);
            highlightCodeLine(palCodeFlow,10); sleep(500);
            highlightCodeLine(palCodeFlow,11); sleep(500);

            highlightCodeLine(palCodeFlow,4); sleep(500);
            highlightCodeLine(palCodeFlow,5); sleep(500);
            highlightCodeLine(palCodeFlow,6); sleep(500);
            highlightCodeLine(palCodeFlow,11); sleep(500);

            Platform.runLater(() -> {
                palConsole.appendText("Text: " + text + "\nResult: " + (isPalindrome?"Palindrome":"Not Palindrome") + "\n");
                palVisual.getChildren().add(new Rectangle(100,40,isPalindrome?Color.GREEN:Color.RED));
            });
            sleep(500);
            highlightCodeLine(palCodeFlow,12);
        }).start();
    }

    @FXML
    private void simulateFibonacci() {
        new Thread(() -> {
            Platform.runLater(() -> { fibConsole.clear(); fibVisual.getChildren().clear(); });
            int n = parseIntSafe(fibInput.getText());
            int a=0,b=1;
            StringBuilder series = new StringBuilder();
            highlightCodeLine(fibCodeFlow,13); sleep(500);
            highlightCodeLine(fibCodeFlow,14); sleep(500);
            highlightCodeLine(fibCodeFlow,15); sleep(500);

            highlightCodeLine(fibCodeFlow,3); sleep(500);
            highlightCodeLine(fibCodeFlow,4); sleep(500); // int a=0,b=1
            for(int i=0;i<n;i++) {
                series.append(a).append(" ");
                highlightCodeLine(fibCodeFlow,5); sleep(300); // loop iteration
                highlightCodeLine(fibCodeFlow,6); sleep(100);
                highlightCodeLine(fibCodeFlow,7); sleep(100);
                highlightCodeLine(fibCodeFlow,8); sleep(100);
                highlightCodeLine(fibCodeFlow,9); sleep(100);
                int c = a+b;
                a=b; b=c;
            }
            highlightCodeLine(fibCodeFlow,15); sleep(500);

            Platform.runLater(() -> {
                fibConsole.appendText("Fibonacci series (" + n + "): " + series + "\n");
                fibVisual.getChildren().add(new Rectangle(120,40,Color.CYAN));
            });
            sleep(500);
            highlightCodeLine(fibCodeFlow,16); // return 0
        }).start();
    }

    @FXML
    private void simulatePrime() {
        new Thread(() -> {
            Platform.runLater(() -> {
                primeConsole.clear();
                primeVisual.getChildren().clear();
            });

            int n = parseIntSafe(primeInput.getText());
            boolean primeFlag = n > 1; // local variable for calculation
            for(int i = 2; i*i <= n && primeFlag; i++)
                if(n % i == 0) primeFlag = false;

            final boolean isPrimeFinal = primeFlag; // make final for lambda

            highlightCodeLine(primeCodeFlow, 10); sleep(500); // if n<=1
            highlightCodeLine(primeCodeFlow, 11); sleep(500); // if n<=1
            highlightCodeLine(primeCodeFlow, 12); sleep(500); // if n<=1


            highlightCodeLine(primeCodeFlow, 3); sleep(500); // if n<=1
            highlightCodeLine(primeCodeFlow, 4); sleep(500); // if n<=1
            highlightCodeLine(primeCodeFlow, 5); sleep(500); // for loop
            highlightCodeLine(primeCodeFlow, 6); sleep(500); // if n%i==0
            highlightCodeLine(primeCodeFlow, 7); sleep(500); // return true

            Platform.runLater(() -> {
                primeConsole.appendText("Number: " + n + "\nResult: " + (isPrimeFinal ? "Prime" : "Not Prime") + "\n");
                primeVisual.getChildren().add(new Rectangle(100, 40, isPrimeFinal ? Color.GREEN : Color.RED));
            });
            highlightCodeLine(primeCodeFlow, 12); sleep(500); // if n<=1

            sleep(500);
            highlightCodeLine(primeCodeFlow, 13); // return 0
        }).start();
    }

}
