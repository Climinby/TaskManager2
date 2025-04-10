module TaskManager.main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires static lombok;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    exports com.climinby.aqiiv;
    opens com.climinby.aqiiv to javafx.base;
}