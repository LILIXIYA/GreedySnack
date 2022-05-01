module com.example.finalproj {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.finalproj to javafx.fxml;
    exports com.example.finalproj;
}