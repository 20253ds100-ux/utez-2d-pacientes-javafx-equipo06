module com.equipo06.pacientes {
    // aca le decimos al proyecto que librerias de JavaFX vamos a usar
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    // estos "opens" son para darle permiso a JavaFX de entrar a nuestras carpetas
    //permiso para que JavaFX lea la carpeta principal
    opens com.equipo06.pacientes to javafx.fxml;
    // permiso para que el FXMLLoader pueda usar nuestro Controlador
    opens com.equipo06.pacientes.controller to javafx.fxml;

    //esto hace que la Tabla lea los datos de la clase Paciente
    opens com.equipo06.pacientes.model to javafx.base;
    // el "exports" sirve para que el programa pueda ejecutarse desde afuera
    exports com.equipo06.pacientes;
}