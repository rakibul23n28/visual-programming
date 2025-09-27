package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StringVisualizerController {

    // ----------- 1. String Length -----------
    @FXML private TextField strLengthInput;
    @FXML private TextArea strLengthCode, strLengthConsole;
    @FXML private FlowPane strLengthVisual;

    @FXML
    private void simulateStringLength() {
        new Thread(() -> {
            Platform.runLater(() -> {
                strLengthConsole.clear();
                strLengthVisual.getChildren().clear();
            });

            final String s = strLengthInput.getText();
            final int len = s.length();

            final String code = String.format("""
                #include <iostream>
                #include <string>
                using namespace std;

                int main() {
                    string s = "%s";
                    cout << "Length = " << s.length();
                    return 0;
                }
            """, s);

            Platform.runLater(() -> {
                strLengthCode.setText(code);
                strLengthConsole.appendText("Input = \"" + s + "\"\nLength = " + len + "\n");
                strLengthVisual.getChildren().add(new Rectangle(len*10+10, 30, Color.LIGHTBLUE));
            });
        }).start();
    }

    // ----------- 2. String Concatenation -----------
    @FXML private TextField concatInput1, concatInput2;
    @FXML private TextArea concatCode, concatConsole;
    @FXML private FlowPane concatVisual;

    @FXML
    private void simulateConcat() {
        new Thread(() -> {
            Platform.runLater(() -> {
                concatConsole.clear();
                concatVisual.getChildren().clear();
            });

            final String s1 = concatInput1.getText();
            final String s2 = concatInput2.getText();
            final String result = s1 + s2;

            final String code = String.format("""
                #include <iostream>
                #include <string>
                using namespace std;

                int main() {
                    string s1 = "%s", s2 = "%s";
                    cout << (s1 + s2);
                    return 0;
                }
            """, s1, s2);

            Platform.runLater(() -> {
                concatCode.setText(code);
                concatConsole.appendText("s1 = \"" + s1 + "\", s2 = \"" + s2 + "\"\nResult = \"" + result + "\"\n");
                concatVisual.getChildren().add(new Rectangle(result.length()*10+10, 30, Color.LIGHTGREEN));
            });
        }).start();
    }

    // ----------- 3. String Comparison -----------
    @FXML private TextField compInput1, compInput2;
    @FXML private TextArea compCode, compConsole;
    @FXML private FlowPane compVisual;

    @FXML
    private void simulateCompare() {
        new Thread(() -> {
            Platform.runLater(() -> {
                compConsole.clear();
                compVisual.getChildren().clear();
            });

            final String s1 = compInput1.getText();
            final String s2 = compInput2.getText();
            final boolean equal = s1.equals(s2);

            final String code = String.format("""
                #include <iostream>
                #include <string>
                using namespace std;

                int main() {
                    string s1 = "%s", s2 = "%s";
                    cout << (s1 == s2 ? "Equal" : "Not Equal");
                    return 0;
                }
            """, s1, s2);

            Platform.runLater(() -> {
                compCode.setText(code);
                compConsole.appendText("s1 = \"" + s1 + "\", s2 = \"" + s2 + "\"\nResult = " + (equal ? "Equal" : "Not Equal") + "\n");
                compVisual.getChildren().add(new Rectangle(120, 30, equal ? Color.GREEN : Color.RED));
            });
        }).start();
    }

    // ----------- 4. Substring Extraction -----------
    @FXML private TextField subInput, subStart, subEnd;
    @FXML private TextArea subCode, subConsole;
    @FXML private FlowPane subVisual;

    @FXML
    private void simulateSubstring() {
        new Thread(() -> {
            Platform.runLater(() -> {
                subConsole.clear();
                subVisual.getChildren().clear();
            });

            final String s = subInput.getText();
            int start, end;
            try { start = Integer.parseInt(subStart.getText().trim()); } catch(Exception e){ start = 0; }
            try { end = Integer.parseInt(subEnd.getText().trim()); } catch(Exception e){ end = s.length(); }

            final int finalStart = Math.max(0, start);
            final int finalEnd = Math.min(s.length(), end);
            final String sub = s.substring(finalStart, finalEnd);

            final String code = String.format("""
                #include <iostream>
                #include <string>
                using namespace std;

                int main() {
                    string s = "%s";
                    cout << s.substr(%d, %d);
                    return 0;
                }
            """, s, finalStart, finalEnd-finalStart);

            Platform.runLater(() -> {
                subCode.setText(code);
                subConsole.appendText("s = \"" + s + "\"\nSubstring = \"" + sub + "\"\n");
                subVisual.getChildren().add(new Rectangle(sub.length()*10+10, 30, Color.ORANGE));
            });
        }).start();
    }

    // ----------- 5. Character Count -----------
    @FXML private TextField charInput;
    @FXML private TextArea charCode, charConsole;
    @FXML private FlowPane charVisual;

    @FXML
    private void simulateCharCount() {
        new Thread(() -> {
            Platform.runLater(() -> {
                charConsole.clear();
                charVisual.getChildren().clear();
            });

            final String s = charInput.getText();
            final int vowels = (int) s.chars().filter(c -> "aeiouAEIOU".indexOf(c) >= 0).count();
            final int consonants = (int) s.chars().filter(c -> Character.isLetter(c) && "aeiouAEIOU".indexOf(c) < 0).count();

            final String code = String.format("""
                #include <iostream>
                #include <string>
                using namespace std;

                int main() {
                    string s = "%s";
                    int vowels=0, consonants=0;
                    for(char c : s) {
                        if(strchr("aeiouAEIOU", c)) vowels++;
                        else if(isalpha(c)) consonants++;
                    }
                    cout << "Vowels = " << vowels << ", Consonants = " << consonants;
                    return 0;
                }
            """, s);

            Platform.runLater(() -> {
                charCode.setText(code);
                charConsole.appendText("Input = \"" + s + "\"\nVowels = " + vowels + ", Consonants = " + consonants + "\n");
                charVisual.getChildren().addAll(
                        new Rectangle(vowels*15+10, 30, Color.BLUE),
                        new Rectangle(consonants*15+10, 30, Color.PURPLE)
                );
            });
        }).start();
    }
}
