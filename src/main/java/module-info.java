module com.music {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfugue;

    opens com.music to javafx.fxml;
    exports com.music;

    opens com.controllers to javafx.fxml;
    exports com.controllers;
}
