module com.example.finalproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.finalproj to javafx.fxml;
    exports com.example.finalproj;
}