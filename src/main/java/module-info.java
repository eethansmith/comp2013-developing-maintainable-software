module comp2013 {
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    exports example;
    opens example to javafx.fxml;
}