package com.example.vlearn;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArrayVisualizerController {

    // ----------- 1D Array Simulation -----------
    @FXML private TextField array1DInput;
    @FXML private TextArea array1DCode, array1DConsole;
    @FXML private FlowPane array1DVisual;

    @FXML
    private void simulate1DArray() {
        new Thread(() -> {
            Platform.runLater(() -> {
                array1DConsole.clear();
                array1DVisual.getChildren().clear();
            });

            String[] values = array1DInput.getText().trim().split("\\s+");
            final int[] arr = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                try {
                    arr[i] = Integer.parseInt(values[i]);
                } catch (Exception e) {
                    arr[i] = 0;
                }
            }

            final StringBuilder visualBuilder = new StringBuilder();
            for (int num : arr) {
                visualBuilder.append(num).append(" ");
            }

            final String code = """
                #include <iostream>
                using namespace std;

                int main() {
                    int arr[] = {%s};
                    int n = sizeof(arr)/sizeof(arr[0]);
                    for(int i=0; i<n; i++)
                        cout << arr[i] << " ";
                    return 0;
                }
                """.formatted(visualBuilder.toString().trim());

            Platform.runLater(() -> {
                array1DCode.setText(code);
                array1DConsole.appendText("Array: " + visualBuilder + "\n");
                for (int num : arr) {
                    array1DVisual.getChildren().add(new Rectangle(40, 40, Color.LIGHTBLUE));
                }
            });
        }).start();
    }

    // ----------- 2D Array Simulation -----------
    @FXML private TextField array2DInput;
    @FXML private TextArea array2DCode, array2DConsole;
    @FXML private FlowPane array2DVisual;

    @FXML
    private void simulate2DArray() {
        new Thread(() -> {
            Platform.runLater(() -> {
                array2DConsole.clear();
                array2DVisual.getChildren().clear();
            });

            String[] rows = array2DInput.getText().trim().split(";");
            int rowCount = rows.length;
            int colCount = 0;
            int[][] arr = new int[rowCount][];
            for (int i = 0; i < rowCount; i++) {
                String[] cols = rows[i].trim().split("\\s+");
                colCount = Math.max(colCount, cols.length);
                arr[i] = new int[cols.length];
                for (int j = 0; j < cols.length; j++) {
                    try {
                        arr[i][j] = Integer.parseInt(cols[j]);
                    } catch (Exception e) {
                        arr[i][j] = 0;
                    }
                }
            }

            StringBuilder consoleBuilder = new StringBuilder();
            StringBuilder codeBuilder = new StringBuilder();
            codeBuilder.append("#include <iostream>\nusing namespace std;\n\nint main() {\n    int arr[][] = {");
            for (int i = 0; i < arr.length; i++) {
                codeBuilder.append("{");
                for (int j = 0; j < arr[i].length; j++) {
                    codeBuilder.append(arr[i][j]);
                    if (j < arr[i].length - 1) codeBuilder.append(", ");
                    consoleBuilder.append(arr[i][j]).append(" ");
                }
                codeBuilder.append("}");
                if (i < arr.length - 1) codeBuilder.append(", ");
                consoleBuilder.append("\n");
            }
            codeBuilder.append("};\n    int rows = ").append(arr.length)
                    .append(", cols = ").append(colCount)
                    .append(";\n    for(int i=0;i<rows;i++){\n        for(int j=0;j<cols;j++)\n            cout << arr[i][j] << \" \";\n        cout << endl;\n    }\n    return 0;\n}");

            Platform.runLater(() -> {
                array2DCode.setText(codeBuilder.toString());
                array2DConsole.appendText(consoleBuilder.toString());
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[i].length; j++) {
                        array2DVisual.getChildren().add(new Rectangle(40, 40, Color.LIGHTGREEN));
                    }
                }
            });
        }).start();
    }
}
