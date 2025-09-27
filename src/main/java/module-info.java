module com.example.vlearn {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.base;
    requires javafx.graphics;

    opens com.example.vlearn to javafx.fxml;
    exports com.example.vlearn;
}