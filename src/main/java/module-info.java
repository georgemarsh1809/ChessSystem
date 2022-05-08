module com.example.chesssys2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.chesssys2 to javafx.fxml;
    exports com.example.chesssys2;
}