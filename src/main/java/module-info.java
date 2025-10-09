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


    opens com.example.vlearn to javafx.fxml;
    exports com.example.vlearn;

    exports com.example.vlearn.controller;
    opens com.example.vlearn.controller to javafx.fxml;
    exports com.example.vlearn.db;
    opens com.example.vlearn.db to javafx.fxml;
    exports com.example.vlearn.models;
    opens com.example.vlearn.models to javafx.fxml;
}