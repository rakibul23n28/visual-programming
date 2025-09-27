package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OperatorVisualizerController {

    // ----------- Arithmetic Operators -----------
    @FXML private TextField arithInput1, arithInput2;
    @FXML private TextArea arithCode, arithConsole;
    @FXML private FlowPane arithVisual;

    @FXML
    private void simulateArithmetic() {
        new Thread(() -> {
            Platform.runLater(() -> {
                arithConsole.clear();
                arithVisual.getChildren().clear();
            });

            int a, b;
            try { a = Integer.parseInt(arithInput1.getText().trim()); } catch(Exception e){ a = 0; }
            try { b = Integer.parseInt(arithInput2.getText().trim()); } catch(Exception e){ b = 0; }

            final int finalA = a;
            final int finalB = b;

            final int sum = finalA + finalB;
            final int diff = finalA - finalB;
            final int prod = finalA * finalB;
            final int quot = finalB != 0 ? finalA / finalB : 0;
            final int mod = finalB != 0 ? finalA % finalB : 0;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int a = %d, b = %d;
                    cout << "a+b = " << a+b << endl;
                    cout << "a-b = " << a-b << endl;
                    cout << "a*b = " << a*b << endl;
                    cout << "a/b = " << (b!=0 ? a/b : 0) << endl;
                    cout << "a%%b = " << (b!=0 ? a%%b : 0) << endl;
                    return 0;
                }
            """, finalA, finalB);

            Platform.runLater(() -> {
                arithCode.setText(code);
                arithConsole.appendText(String.format("a = %d, b = %d\n", finalA, finalB));
                arithConsole.appendText(String.format("a+b = %d\na-b = %d\na*b = %d\na/b = %d\na%%b = %d\n",
                        sum, diff, prod, quot, mod));
                arithVisual.getChildren().addAll(
                        new Rectangle(Math.abs(sum)*10+10, 20, Color.LIGHTBLUE),
                        new Rectangle(Math.abs(diff)*10+10, 20, Color.LIGHTGREEN),
                        new Rectangle(Math.abs(prod)*5+10, 20, Color.ORANGE),
                        new Rectangle(Math.abs(quot)*20+10, 20, Color.PURPLE),
                        new Rectangle(Math.abs(mod)*20+10, 20, Color.RED)
                );
            });
        }).start();
    }

    // ----------- Relational Operators -----------
    @FXML private TextField relInput1, relInput2;
    @FXML private TextArea relCode, relConsole;
    @FXML private FlowPane relVisual;

    @FXML
    private void simulateRelational() {
        new Thread(() -> {
            Platform.runLater(() -> {
                relConsole.clear();
                relVisual.getChildren().clear();
            });

            int a, b;
            try { a = Integer.parseInt(relInput1.getText().trim()); } catch(Exception e){ a = 0; }
            try { b = Integer.parseInt(relInput2.getText().trim()); } catch(Exception e){ b = 0; }

            final int finalA = a;
            final int finalB = b;

            final boolean eq = finalA == finalB;
            final boolean neq = finalA != finalB;
            final boolean gt = finalA > finalB;
            final boolean lt = finalA < finalB;
            final boolean gte = finalA >= finalB;
            final boolean lte = finalA <= finalB;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int a = %d, b = %d;
                    cout << (a==b) << " " << (a!=b) << " " << (a>b) << " " << (a<b) << " " << (a>=b) << " " << (a<=b);
                    return 0;
                }
            """, finalA, finalB);

            Platform.runLater(() -> {
                relCode.setText(code);
                relConsole.appendText(String.format("a = %d, b = %d\n", finalA, finalB));
                relConsole.appendText(String.format("a==b: %b, a!=b: %b, a>b: %b, a<b: %b, a>=b: %b, a<=b: %b\n",
                        eq, neq, gt, lt, gte, lte));

                relVisual.getChildren().addAll(
                        new Rectangle(40, 20, eq ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, neq ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, gt ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, lt ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, gte ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, lte ? Color.GREEN : Color.RED)
                );
            });
        }).start();
    }

    // ----------- Logical Operators -----------
    @FXML private TextField logInput1, logInput2;
    @FXML private TextArea logCode, logConsole;
    @FXML private FlowPane logVisual;

    @FXML
    private void simulateLogical() {
        new Thread(() -> {
            Platform.runLater(() -> {
                logConsole.clear();
                logVisual.getChildren().clear();
            });

            final boolean a = logInput1.getText().trim().equals("1");
            final boolean b = logInput2.getText().trim().equals("1");

            final boolean and = a && b;
            final boolean or = a || b;
            final boolean notA = !a;
            final boolean notB = !b;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    bool a = %b, b = %b;
                    cout << (a&&b) << " " << (a||b) << " " << (!a) << " " << (!b);
                    return 0;
                }
            """, a, b);

            Platform.runLater(() -> {
                logCode.setText(code);
                logConsole.appendText(String.format("a = %b, b = %b\n", a, b));
                logConsole.appendText(String.format("a&&b: %b, a||b: %b, !a: %b, !b: %b\n", and, or, notA, notB));

                logVisual.getChildren().addAll(
                        new Rectangle(40, 20, and ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, or ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, notA ? Color.GREEN : Color.RED),
                        new Rectangle(40, 20, notB ? Color.GREEN : Color.RED)
                );
            });
        }).start();
    }

    // ----------- Bitwise Operators -----------
    @FXML private TextField bitInput1, bitInput2;
    @FXML private TextArea bitCode, bitConsole;
    @FXML private FlowPane bitVisual;

    @FXML
    private void simulateBitwise() {
        new Thread(() -> {
            Platform.runLater(() -> {
                bitConsole.clear();
                bitVisual.getChildren().clear();
            });

            int a, b;
            try { a = Integer.parseInt(bitInput1.getText().trim()); } catch(Exception e){ a = 0; }
            try { b = Integer.parseInt(bitInput2.getText().trim()); } catch(Exception e){ b = 0; }

            final int finalA = a;
            final int finalB = b;

            final int and = finalA & finalB;
            final int or = finalA | finalB;
            final int xor = finalA ^ finalB;
            final int notA = ~finalA;
            final int left = finalA << 1;
            final int right = finalA >> 1;

            final String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int a = %d, b = %d;
                    cout << (a&b) << " " << (a|b) << " " << (a^b) << " " << (~a) << " " << (a<<1) << " " << (a>>1);
                    return 0;
                }
            """, finalA, finalB);

            Platform.runLater(() -> {
                bitCode.setText(code);
                bitConsole.appendText(String.format("a = %d, b = %d\n", finalA, finalB));
                bitConsole.appendText(String.format("a&b: %d, a|b: %d, a^b: %d, ~a: %d, a<<1: %d, a>>1: %d\n",
                        and, or, xor, notA, left, right));

                bitVisual.getChildren().addAll(
                        new Rectangle(40, 20, Color.BLUE),
                        new Rectangle(40, 20, Color.GREEN),
                        new Rectangle(40, 20, Color.ORANGE),
                        new Rectangle(40, 20, Color.RED),
                        new Rectangle(40, 20, Color.PURPLE),
                        new Rectangle(40, 20, Color.BROWN)
                );
            });
        }).start();
    }
}
