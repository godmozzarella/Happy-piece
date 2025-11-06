module org.example.javafx_02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens org.example.javafx_02 to javafx.fxml;
    exports org.example.javafx_02;
}