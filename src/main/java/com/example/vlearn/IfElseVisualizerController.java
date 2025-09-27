package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class IfElseVisualizerController {

    // ----------- Even/Odd -----------
    @FXML private TextField evenOddInput;
    @FXML private TextArea evenOddConsole;
    @FXML private TextArea evenOddCode;
    @FXML private FlowPane evenOddVisual;

    @FXML
    private void checkEvenOdd() {
        new Thread(() -> {
            Platform.runLater(() -> {
                evenOddConsole.clear();
                evenOddVisual.getChildren().clear();
            });

            int n;
            try {
                n = Integer.parseInt(evenOddInput.getText().trim());
            } catch (Exception e) {
                n = 0;
            }

            final int val = n;
            final boolean even = (n % 2 == 0);

            String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int n = %d;
                    if(n %% 2 == 0) {
                        cout << "Even";
                    } else {
                        cout << "Odd";
                    }
                    return 0;
                }
                """, n);

            Platform.runLater(() -> {
                evenOddCode.setText(code);
                evenOddConsole.appendText("Input: " + val + "\n");
                if (even) {
                    evenOddConsole.appendText("n % 2 == 0 → Even\n");
                    evenOddVisual.getChildren().add(new Rectangle(60, 40, Color.GREEN));
                } else {
                    evenOddConsole.appendText("n % 2 != 0 → Odd\n");
                    evenOddVisual.getChildren().add(new Rectangle(60, 40, Color.RED));
                }
            });
        }).start();
    }

    // ----------- Grade Calculator -----------
    @FXML private TextField gradeInput;
    @FXML private TextArea gradeCode, gradeConsole;
    @FXML private FlowPane gradeVisual;

    @FXML
    private void calculateGrade() {
        new Thread(() -> {
            Platform.runLater(() -> {
                gradeConsole.clear();
                gradeVisual.getChildren().clear();
            });

            int marks;
            try {
                marks = Integer.parseInt(gradeInput.getText().trim());
            } catch (Exception e) {
                marks = 0;
            }

            final int val = marks;
            final String grade;
            if (marks >= 80) grade = "A";
            else if (marks >= 60) grade = "B";
            else if (marks >= 40) grade = "C";
            else grade = "F";

            String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int marks = %d;
                    if(marks >= 80) cout << "A";
                    else if(marks >= 60) cout << "B";
                    else if(marks >= 40) cout << "C";
                    else cout << "F";
                    return 0;
                }
                """, marks);

            final String resultGrade = grade;

            Platform.runLater(() -> {
                gradeCode.setText(code);
                gradeConsole.appendText("Marks = " + val + "\nGrade = " + resultGrade + "\n");

                Color color = switch (resultGrade) {
                    case "A" -> Color.GREEN;
                    case "B" -> Color.BLUE;
                    case "C" -> Color.ORANGE;
                    default -> Color.RED;
                };
                gradeVisual.getChildren().add(new Rectangle(60, 40, color));
            });
        }).start();
    }

    // ----------- Leap Year Checker -----------
    @FXML private TextField leapYearInput;
    @FXML private TextArea leapYearCode, leapYearConsole;
    @FXML private FlowPane leapYearVisual;

    @FXML
    private void checkLeapYear() {
        new Thread(() -> {
            Platform.runLater(() -> {
                leapYearConsole.clear();
                leapYearVisual.getChildren().clear();
            });

            int year;
            try {
                year = Integer.parseInt(leapYearInput.getText().trim());
            } catch (Exception e) {
                year = 0;
            }

            final int val = year;
            final boolean isLeap = (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);

            String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int year = %d;
                    if((year %% 400 == 0) || (year %% 4 == 0 && year %% 100 != 0)) {
                        cout << "Leap Year";
                    } else {
                        cout << "Not Leap Year";
                    }
                    return 0;
                }
                """, year);

            Platform.runLater(() -> {
                leapYearCode.setText(code);
                leapYearConsole.appendText("Year = " + val + "\n");
                leapYearConsole.appendText(isLeap ? "Leap Year\n" : "Not a Leap Year\n");
                leapYearVisual.getChildren().add(new Rectangle(60, 40, isLeap ? Color.GREEN : Color.RED));
            });
        }).start();
    }

    // ----------- Discount Calculator -----------
    @FXML private TextField discountPriceInput, discountPercentInput;
    @FXML private TextArea discountCode, discountConsole;
    @FXML private FlowPane discountVisual;

    @FXML
    private void calculateDiscount() {
        new Thread(() -> {
            Platform.runLater(() -> {
                discountConsole.clear();
                discountVisual.getChildren().clear();
            });

            double price, percent;
            try {
                price = Double.parseDouble(discountPriceInput.getText().trim());
                percent = Double.parseDouble(discountPercentInput.getText().trim());
            } catch (Exception e) {
                price = 0;
                percent = 0;
            }

            final double p = price, d = percent;
            final double f = price - (price * percent / 100);

            String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    double price = %.2f;
                    double discount = %.2f;
                    double finalPrice = price - (price * discount / 100);
                    cout << finalPrice;
                    return 0;
                }
                """, p, d);

            Platform.runLater(() -> {
                discountCode.setText(code);
                discountConsole.appendText("Price = " + p + "\nDiscount = " + d + "%\nFinal Price = " + f + "\n");
                discountVisual.getChildren().add(new Rectangle(80, 40, Color.ORANGE));
            });
        }).start();
    }

    // ----------- Largest Number -----------
    @FXML private TextField largestInput1, largestInput2, largestInput3;
    @FXML private TextArea largestCode, largestConsole;
    @FXML private FlowPane largestVisual;

    @FXML
    private void findLargest() {
        new Thread(() -> {
            Platform.runLater(() -> {
                largestConsole.clear();
                largestVisual.getChildren().clear();
            });

            int a, b, c;
            try {
                a = Integer.parseInt(largestInput1.getText().trim());
                b = Integer.parseInt(largestInput2.getText().trim());
                c = Integer.parseInt(largestInput3.getText().trim());
            } catch (Exception e) {
                a = b = c = 0;
            }

            final int fa = a, fb = b, fc = c;
            final int largest = Math.max(fa, Math.max(fb, fc));

            String code = String.format("""
                #include <iostream>
                using namespace std;

                int main() {
                    int a = %d;
                    int b = %d;
                    int c = %d;
                    int largest = max(a, max(b, c));
                    cout << largest;
                    return 0;
                }
                """, fa, fb, fc);

            Platform.runLater(() -> {
                largestCode.setText(code);
                largestConsole.appendText("Numbers = " + fa + ", " + fb + ", " + fc + "\n");
                largestConsole.appendText("Largest = " + largest + "\n");
                largestVisual.getChildren().add(new Rectangle(80, 40, Color.GREEN));
            });
        }).start();
    }

    // ----------- Login Simulation -----------
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private TextArea loginCode, loginConsole;
    @FXML private FlowPane loginVisual;

    @FXML
    private void loginSimulation() {
        new Thread(() -> {
            Platform.runLater(() -> {
                loginConsole.clear();
                loginVisual.getChildren().clear();
            });

            final String u = loginUsername.getText().trim();
            final String p = loginPassword.getText().trim();
            final boolean success = u.equals("admin") && p.equals("1234");

            String code = """
                #include<iostream>
                using namespace std;

                int main() {
                    string username, password;
                    cin >> username >> password;
                    if(username == "admin" && password == "1234") {
                        cout << "Login Success";
                    } else {
                        cout << "Login Failed";
                    }
                    return 0;
                }
                """;

            Platform.runLater(() -> {
                loginCode.setText(code);
                loginConsole.appendText("Username = " + u + "\nPassword = " + p + "\n");
                loginConsole.appendText(success ? "Login Success\n" : "Login Failed\n");
                loginVisual.getChildren().add(new Rectangle(80, 40, success ? Color.GREEN : Color.RED));
            });
        }).start();
    }
}
