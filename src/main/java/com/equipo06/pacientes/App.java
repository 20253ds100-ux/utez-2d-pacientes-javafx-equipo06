package com.equipo06.pacientes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Esta línea busca tu archivo FXML en la carpeta de resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("pacientes-view.fxml"));

        // Creamos la ventana (Ancho 850, Alto 550)
        Scene scene = new Scene(fxmlLoader.load(), 850, 550);

        stage.setTitle("Consultorio Médico - Equipo 06");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
