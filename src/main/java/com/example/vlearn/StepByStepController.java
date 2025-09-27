package com.example.vlearn;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StepByStepController {

    @FXML
    private TextArea txtSteps;

    private List<String> steps = new ArrayList<>();
    private int currentStep = 0;

    private int number;

    @FXML
    public void initialize() {
        startSimulation();
    }

    private void startSimulation() {
        // Ask user for input
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Even or Odd Step-by-Step");
        dialog.setHeaderText("Enter a number to check:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            try {
                number = Integer.parseInt(input);
                prepareSteps(number);
                txtSteps.setText(steps.get(0));
                currentStep = 1;
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.", Color.RED);
            }
        });
    }

    private void prepareSteps(int num) {
        steps.clear();
        steps.add("Step 1: You entered the number: " + num);
        steps.add("Step 2: Check if the number is divisible by 2 using num % 2");
        steps.add("Step 3: num % 2 = " + (num % 2));
        if (num % 2 == 0) {
            steps.add("Step 4: Result is 0, so the number is Even ✅");
        } else {
            steps.add("Step 4: Result is not 0, so the number is Odd ❌");
        }
        steps.add("Step 5: Simulation Complete 🎉");
    }

    @FXML
    private void nextStep() {
        if (currentStep < steps.size()) {
            txtSteps.appendText("\n" + steps.get(currentStep));
            currentStep++;
        } else {
            showAlert("Info", "You have completed the simulation.", Color.GREEN);
        }
    }

    private void showAlert(String title, String message, Color color) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        Text text = new Text(message);
        text.setFill(color);
        text.setFont(Font.font("Arial", 16));

        alert.getDialogPane().setContent(text);
        alert.showAndWait();
    }
}
