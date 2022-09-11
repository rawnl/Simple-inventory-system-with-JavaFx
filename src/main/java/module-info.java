module com.example {
    requires javafx.controls;
    requires com.jfoenix;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.model to javafx.base;
    opens com.example to javafx.fxml;
    opens com.example.db;
    exports com.example;
}
