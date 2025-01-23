module inv {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens gucant.inv to javafx.fxml;
    exports gucant.inv;
}
