module com.example.vlearn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires MaterialFX;
    requires eu.hansolo.tilesfx;
    requires com.hivemq.client.mqtt;
    requires com.fasterxml.jackson.databind;
<<<<<<< HEAD
    requires java.prefs;
=======
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e


    opens com.example.vlearn to javafx.fxml;
    exports com.example.vlearn;

    exports com.example.vlearn.controller;
    opens com.example.vlearn.controller to javafx.fxml;
    exports com.example.vlearn.db;
    opens com.example.vlearn.db to javafx.fxml;
    exports com.example.vlearn.models;
    opens com.example.vlearn.models to javafx.fxml;
<<<<<<< HEAD
    exports com.example.vlearn.controller.programming_basic;
    opens com.example.vlearn.controller.programming_basic to javafx.fxml;
    exports com.example.vlearn.controller.dataStructure;
    opens com.example.vlearn.controller.dataStructure to javafx.fxml;
    exports com.example.vlearn.controller.algorithm;
    opens com.example.vlearn.controller.algorithm to javafx.fxml;
=======
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
}