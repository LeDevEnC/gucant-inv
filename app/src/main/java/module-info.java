module inv {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    opens gucant.inv to javafx.fxml;
    exports gucant.inv;
}
