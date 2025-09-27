package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FunctionVisualizerController {

    // --- 1. Addition Function ---
    @FXML private TextField addInput1, addInput2;
    @FXML private TextArea addCode, addConsole;
    @FXML private FlowPane addVisual;

    @FXML
    private void additionFunction() {
        new Thread(() -> {
            Platform.runLater(() -> {
                addConsole.clear();
                addVisual.getChildren().clear();
            });

            int num1, num2;
            try { num1 = Integer.parseInt(addInput1.getText().trim()); } catch(Exception e){ num1 = 0; }
            try { num2 = Integer.parseInt(addInput2.getText().trim()); } catch(Exception e){ num2 = 0; }
            final int a = num1, b = num2;
            final int sum = a + b;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int add(int x, int y) {
                    return x + y;
                }

                int main() {
                    int a = %d, b = %d;
                    cout << add(a, b);
                    return 0;
                }
                """, a, b);

            Platform.runLater(() -> {
                addCode.setText(code);
                addConsole.appendText("a = " + a + ", b = " + b + "\nSum = " + sum + "\n");
                addVisual.getChildren().add(new Rectangle(80, 40, Color.GREEN));
            });
        }).start();
    }

    // --- 2. Factorial Function ---
    @FXML private TextField factInput;
    @FXML private TextArea factCode, factConsole;
    @FXML private FlowPane factVisual;

    @FXML
    private void factorialFunction() {
        new Thread(() -> {
            Platform.runLater(() -> {
                factConsole.clear();
                factVisual.getChildren().clear();
            });

            int n;
            try { n = Integer.parseInt(factInput.getText().trim()); } catch(Exception e){ n = 0; }
            final int num = n;

            int result = 1;
            for(int i = 1; i <= num; i++) result *= i;
            final int factorial = result;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int factorial(int n) {
                    int result = 1;
                    for(int i=1; i<=n; i++) result *= i;
                    return result;
                }

                int main() {
                    int n = %d;
                    cout << factorial(n);
                    return 0;
                }
                """, num);

            Platform.runLater(() -> {
                factCode.setText(code);
                factConsole.appendText("n = " + num + "\nFactorial = " + factorial + "\n");
                factVisual.getChildren().add(new Rectangle(80, 40, Color.ORANGE));
            });
        }).start();
    }

    // --- 3. Palindrome Checker Function ---
    @FXML private TextField palInput;
    @FXML private TextArea palCode, palConsole;
    @FXML private FlowPane palVisual;

    @FXML
    private void palindromeFunction() {
        new Thread(() -> {
            Platform.runLater(() -> {
                palConsole.clear();
                palVisual.getChildren().clear();
            });

            final String text = palInput.getText().trim();
            final boolean isPalindrome = text.equalsIgnoreCase(new StringBuilder(text).reverse().toString());

            final String code = String.format("""
                #include <iostream>
                #include <string>
                using namespace std;

                bool isPalindrome(string s) {
                    string rev = string(s.rbegin(), s.rend());
                    return s == rev;
                }

                int main() {
                    string text = "%s";
                    cout << (isPalindrome(text) ? "Palindrome" : "Not Palindrome");
                    return 0;
                }
                """, text);

            Platform.runLater(() -> {
                palCode.setText(code);
                palConsole.appendText("Text = \"" + text + "\"\nResult = " + (isPalindrome ? "Palindrome" : "Not Palindrome") + "\n");
                palVisual.getChildren().add(new Rectangle(100, 40, isPalindrome ? Color.GREEN : Color.RED));
            });
        }).start();
    }

    // --- 4. Fibonacci Function ---
    @FXML private TextField fibInput;
    @FXML private TextArea fibCode, fibConsole;
    @FXML private FlowPane fibVisual;

    @FXML
    private void fibonacciFunction() {
        new Thread(() -> {
            Platform.runLater(() -> {
                fibConsole.clear();
                fibVisual.getChildren().clear();
            });

            int n;
            try { n = Integer.parseInt(fibInput.getText().trim()); } catch(Exception e){ n = 0; }
            final int num = n;

            StringBuilder series = new StringBuilder();
            int a = 0, b = 1;
            for(int i=0; i<num; i++) {
                series.append(a).append(" ");
                int c = a + b;
                a = b;
                b = c;
            }
            final String fibSeries = series.toString().trim();

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                void fibonacci(int n) {
                    int a=0, b=1;
                    for(int i=0; i<n; i++) {
                        cout << a << " ";
                        int c = a + b;
                        a = b;
                        b = c;
                    }
                }

                int main() {
                    int n = %d;
                    fibonacci(n);
                    return 0;
                }
                """, num);

            Platform.runLater(() -> {
                fibCode.setText(code);
                fibConsole.appendText("First " + num + " Fibonacci numbers: " + fibSeries + "\n");
                fibVisual.getChildren().add(new Rectangle(120, 40, Color.CYAN));
            });
        }).start();
    }

    // --- 5. Prime Checker Function ---
    @FXML private TextField primeInput;
    @FXML private TextArea primeCode, primeConsole;
    @FXML private FlowPane primeVisual;

    @FXML
    private void primeFunction() {
        new Thread(() -> {
            Platform.runLater(() -> {
                primeConsole.clear();
                primeVisual.getChildren().clear();
            });

            int n;
            try { n = Integer.parseInt(primeInput.getText().trim()); } catch(Exception e){ n = 0; }
            final int num = n;

            boolean isPrime = n > 1;
            for(int i=2; i*i <= n && isPrime; i++) {
                if(n % i == 0) isPrime = false;
            }
            final boolean primeResult = isPrime;

            final String code = String.format("""
    #include <iostream>
    using namespace std;

    bool isPrime(int n) {
        if(n<=1) return false;
        for(int i=2; i*i<=n; i++)
            if(n %% i == 0) return false;
        return true;
    }

    int main() {
        int n = %d;
        cout << (isPrime(n) ? "Prime" : "Not Prime");
        return 0;
    }
    """, num);


            Platform.runLater(() -> {
                primeCode.setText(code);
                primeConsole.appendText("Number = " + num + "\nResult = " + (primeResult ? "Prime" : "Not Prime") + "\n");
                primeVisual.getChildren().add(new Rectangle(100, 40, primeResult ? Color.GREEN : Color.RED));
            });
        }).start();
    }
}
