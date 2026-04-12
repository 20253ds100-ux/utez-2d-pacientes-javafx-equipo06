module com.equipo06.pacientes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.equipo06.pacientes to javafx.fxml;
    opens com.equipo06.pacientes.controller to javafx.fxml;
    opens com.equipo06.pacientes.model to javafx.base;

    exports com.equipo06.pacientes;
}