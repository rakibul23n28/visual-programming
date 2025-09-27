package com.example.vlearn;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;

public class VisualizerController {

    @FXML private TabPane memoryTabPane;

    private static class TypeInfo {
        int size;
        String color;
        String description;
        String valueExample;
        String range;
        String codeExample;

        TypeInfo(int size, String color, String description, String valueExample, String range, String codeExample) {
            this.size = size;
            this.color = color;
            this.description = description;
            this.valueExample = valueExample;
            this.range = range;
            this.codeExample = codeExample;
        }
    }

    private final Map<String, TypeInfo> dataTypes = new LinkedHashMap<>();

    @FXML
    private void initialize() {
        // Define C++ types with soft pastel color accents
        dataTypes.put("int", new TypeInfo(4, "#d1fae5", "Integer. Stores whole numbers.", "1,2,3,4,...", "-2,147,483,648 to 2,147,483,647", "int x = 10;\nint y = 20;"));
        dataTypes.put("char", new TypeInfo(1, "#fff7db", "Character. Stores a single character.", "'a','b','c'", "-128 to 127", "char c = 'A';"));
        dataTypes.put("float", new TypeInfo(4, "#fee2e2", "Floating-point number with decimals.", "1.0, 2.5, 3.14", "1.2E-38 to 3.4E+38", "float f = 3.14f;"));
        dataTypes.put("double", new TypeInfo(8, "#dbeafe", "Double-precision floating-point.", "1.0, 2.5, 3.14159", "2.3E-308 to 1.7E+308", "double d = 3.14159;"));
        dataTypes.put("bool", new TypeInfo(1, "#ede9fe", "Boolean. Stores true or false.", "true, false", "0 or 1", "bool flag = true;"));
        dataTypes.put("long", new TypeInfo(8, "#dcfce7", "Long integer, larger range than int.", "1000000000L", "-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807", "long l = 1000000000L;"));
        dataTypes.put("short", new TypeInfo(2, "#fee2e2", "Short integer, smaller range than int.", "10,20,30", "-32,768 to 32,767", "short s = 100;"));
        dataTypes.put("string", new TypeInfo(24, "#fef3c7", "String. Sequence of characters.", "\"Hello, World!\"", "N/A", "std::string str = \"Hello\";"));

        populateTabs();
    }

    private void populateTabs() {
        memoryTabPane.getTabs().clear();

        for (Map.Entry<String, TypeInfo> entry : dataTypes.entrySet()) {
            String typeName = entry.getKey();
            TypeInfo info = entry.getValue();

            VBox content = new VBox(12);
            content.setStyle("-fx-padding: 15px;");

            content.getChildren().addAll(
                    createMinimalBlock("Description", info.description, info.color),
                    createMinimalBlock("Size", info.size + " bytes", info.color),
                    createMinimalBlock("Example Values", info.valueExample, info.color),
                    createMinimalBlock("Range", info.range, info.color),
                    createMinimalBlock("C/C++ Code", info.codeExample, info.color)
            );

            ScrollPane scroll = new ScrollPane(content);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background-color:transparent;");

            Tab tab = new Tab(typeName, scroll);
            memoryTabPane.getTabs().add(tab);
        }
    }

    private VBox createMinimalBlock(String title, String content, String color) {
        VBox block = new VBox(5);
        block.setStyle(
                "-fx-background-color:" + color + ";" +
                        "-fx-background-radius:8;" +
                        "-fx-padding:10;" +
                        "-fx-border-color:#ccc;" +
                        "-fx-border-width:1;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight:bold; -fx-text-fill:#333; -fx-font-size:13px;");

        Label contentLabel = new Label(content);
        contentLabel.setStyle("-fx-font-size:12px; -fx-text-fill:#333;");
        contentLabel.setWrapText(true);

        block.getChildren().addAll(titleLabel, contentLabel);
        return block;
    }
}
