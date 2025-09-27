package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class IOVisualizerController {

    // --- 1. Echo Input ---
    @FXML private TextField echoInput;
    @FXML private TextArea echoCode, echoConsole;
    @FXML private FlowPane echoVisual;

    @FXML
    private void simulateEcho() {
        new Thread(() -> {
            Platform.runLater(() -> {
                echoConsole.clear();
                echoVisual.getChildren().clear();
            });

            final String text = echoInput.getText().trim();

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    string text = "%s";
                    cout << text;
                    return 0;
                }
                """, text);

            Platform.runLater(() -> {
                echoCode.setText(code);
                echoConsole.appendText("Input: " + text + "\nOutput: " + text + "\n");
                echoVisual.getChildren().add(new Rectangle(Math.max(text.length()*10, 50), 40, Color.LIGHTBLUE));
            });
        }).start();
    }

    // --- 2. Sum Two Numbers ---
    @FXML private TextField sumInput1, sumInput2;
    @FXML private TextArea sumCode, sumConsole;
    @FXML private FlowPane sumVisual;

    @FXML
    private void simulateSum() {
        new Thread(() -> {
            Platform.runLater(() -> {
                sumConsole.clear();
                sumVisual.getChildren().clear();
            });

            final int a, b;
            try { a = Integer.parseInt(sumInput1.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }
            try { b = Integer.parseInt(sumInput2.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }

            final int sum = a + b;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int a = %d, b = %d;
                    cout << (a + b);
                    return 0;
                }
                """, a, b);

            Platform.runLater(() -> {
                sumCode.setText(code);
                sumConsole.appendText("Input: a=" + a + ", b=" + b + "\nSum = " + sum + "\n");
                sumVisual.getChildren().add(new Rectangle(sum*10+10, 40, Color.LIGHTGREEN));
            });
        }).start();
    }

    // --- 3. Maximum of Three Numbers ---
    @FXML private TextField maxInput1, maxInput2, maxInput3;
    @FXML private TextArea maxCode, maxConsole;
    @FXML private FlowPane maxVisual;

    @FXML
    private void simulateMax() {
        new Thread(() -> {
            Platform.runLater(() -> {
                maxConsole.clear();
                maxVisual.getChildren().clear();
            });

            final int a, b, c;
            try { a = Integer.parseInt(maxInput1.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }
            try { b = Integer.parseInt(maxInput2.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }
            try { c = Integer.parseInt(maxInput3.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }

            final int largest = Math.max(a, Math.max(b, c));

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int a = %d, b = %d, c = %d;
                    int largest = max(a,max(b,c));
                    cout << largest;
                    return 0;
                }
                """, a, b, c);

            Platform.runLater(() -> {
                maxCode.setText(code);
                maxConsole.appendText("Inputs: " + a + ", " + b + ", " + c + "\nLargest = " + largest + "\n");
                maxVisual.getChildren().add(new Rectangle(largest*10+10, 40, Color.ORANGE));
            });
        }).start();
    }

    // --- 4. Even or Odd ---
    @FXML private TextField evenOddInput;
    @FXML private TextArea evenOddCode, evenOddConsole;
    @FXML private FlowPane evenOddVisual;

    @FXML
    private void simulateEvenOdd() {
        new Thread(() -> {
            Platform.runLater(() -> {
                evenOddConsole.clear();
                evenOddVisual.getChildren().clear();
            });

            final int n;
            try { n = Integer.parseInt(evenOddInput.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }

            final boolean isEven = n % 2 == 0;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int n = %d;
                    if(n%%2==0) cout << "Even";
                    else cout << "Odd";
                    return 0;
                }
                """, n);

            Platform.runLater(() -> {
                evenOddCode.setText(code);
                evenOddConsole.appendText("Input: " + n + "\nOutput: " + (isEven ? "Even" : "Odd") + "\n");
                evenOddVisual.getChildren().add(new Rectangle(80, 40, isEven ? Color.GREEN : Color.RED));
            });
        }).start();
    }

    // --- 5. Factorial ---
    @FXML private TextField factInput;
    @FXML private TextArea factCode, factConsole;
    @FXML private FlowPane factVisual;

    @FXML
    private void simulateFactorial() {
        new Thread(() -> {
            Platform.runLater(() -> {
                factConsole.clear();
                factVisual.getChildren().clear();
            });

            final int n;
            try { n = Integer.parseInt(factInput.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }

            long factorial = 1;
            for(int i=2;i<=n;i++) factorial *= i;
            final long fact = factorial;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int n = %d;
                    long fact = 1;
                    for(int i=2;i<=n;i++) fact *= i;
                    cout << fact;
                    return 0;
                }
                """, n);

            Platform.runLater(() -> {
                factCode.setText(code);
                factConsole.appendText("Input: " + n + "\nFactorial = " + fact + "\n");
                factVisual.getChildren().add(new Rectangle(Math.min(fact*2,300), 40, Color.PURPLE));
            });
        }).start();
    }

    // --- 6. Fibonacci Series ---
    @FXML private TextField fibInput;
    @FXML private TextArea fibCode, fibConsole;
    @FXML private FlowPane fibVisual;

    @FXML
    private void simulateFibonacci() {
        new Thread(() -> {
            Platform.runLater(() -> {
                fibConsole.clear();
                fibVisual.getChildren().clear();
            });

            final int n;
            try { n = Integer.parseInt(fibInput.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }

            int[] series = new int[n];
            if(n>0) series[0]=0;
            if(n>1) series[1]=1;
            for(int i=2;i<n;i++) series[i] = series[i-1] + series[i-2];

            final StringBuilder fibStr = new StringBuilder();
            for(int num : series) fibStr.append(num).append(" ");

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int n = %d;
                    int a=0,b=1,c;
                    for(int i=0;i<n;i++){
                        if(i==0) cout << a << " ";
                        else if(i==1) cout << b << " ";
                        else { c=a+b; cout << c << " "; a=b; b=c;}
                    }
                    return 0;
                }
                """, n);

            Platform.runLater(() -> {
                fibCode.setText(code);
                fibConsole.appendText("n = " + n + "\nSeries: " + fibStr + "\n");
                for(int num : series){
                    fibVisual.getChildren().add(new Rectangle(Math.min(num*10+10,100), 20, Color.LIGHTBLUE));
                }
            });
        }).start();
    }

    // --- 7. Prime Check ---
    @FXML private TextField primeInput;
    @FXML private TextArea primeCode, primeConsole;
    @FXML private FlowPane primeVisual;

    @FXML
    private void simulatePrime() {
        new Thread(() -> {
            Platform.runLater(() -> {
                primeConsole.clear();
                primeVisual.getChildren().clear();
            });

            final int n;
            try { n = Integer.parseInt(primeInput.getText().trim()); } catch(Exception e) { throw new RuntimeException("Invalid input"); }

            boolean isPrime = n>1;
            for(int i=2;i*i<=n;i++){
                if(n%i==0) { isPrime=false; break; }
            }

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                bool isPrime(int n){
                    if(n<=1) return false;
                    for(int i=2;i*i<=n;i++)
                        if(n%%i==0) return false;
                    return true;
                }

                int main() {
                    int n = %d;
                    cout << (isPrime(n) ? "Prime" : "Not Prime");
                    return 0;
                }
                """, n);

            final boolean prime = isPrime;

            Platform.runLater(() -> {
                primeCode.setText(code);
                primeConsole.appendText("Input: " + n + "\nOutput: " + (prime ? "Prime" : "Not Prime") + "\n");
                primeVisual.getChildren().add(new Rectangle(80, 40, prime ? Color.GREEN : Color.RED));
            });
        }).start();
    }
}
