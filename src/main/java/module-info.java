module com.example.vlearn {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires MaterialFX;
    requires eu.hansolo.tilesfx;
    requires com.hivemq.client.mqtt;
    requires com.fasterxml.jackson.databind;
    requires java.prefs; // Optional, only if you use Preferences API

    // Main application package
    opens com.example.vlearn to javafx.fxml;
    exports com.example.vlearn;

    // Controller packages
    exports com.example.vlearn.controller;
    opens com.example.vlearn.controller to javafx.fxml;

    exports com.example.vlearn.controller.programming_basic;
    opens com.example.vlearn.controller.programming_basic to javafx.fxml;

    exports com.example.vlearn.controller.dataStructure;
    opens com.example.vlearn.controller.dataStructure to javafx.fxml;

    exports com.example.vlearn.controller.algorithm;
    opens com.example.vlearn.controller.algorithm to javafx.fxml;

    // Database and models
    exports com.example.vlearn.db;
    opens com.example.vlearn.db to javafx.fxml;

    exports com.example.vlearn.models;
    opens com.example.vlearn.models to javafx.fxml;
}
