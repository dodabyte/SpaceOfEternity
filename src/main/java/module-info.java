module com.example.spaceofeternity {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;

    exports com.example.spaceofeternity.application;
    opens com.example.spaceofeternity.application to javafx.fxml;
}