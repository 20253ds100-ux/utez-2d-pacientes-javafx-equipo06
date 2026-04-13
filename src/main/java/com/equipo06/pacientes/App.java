package com.equipo06.pacientes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // esta linea busca el archivo de diseño (el FXML) que hicimos para las pestañas
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("pacientes-view.fxml"));

        //aqui creamos la escena (pestana) y le damos el tamaño a la ventana
        // El 850 es el ancho y 550 es el alto
        Scene scene = new Scene(fxmlLoader.load(), 850, 550);

        //le ponemos el titulo que se vera arriba en la ventanita
        stage.setTitle("Consultorio Medico - Equipo 06");

        // metemos la escena en el escenario (stage)
        stage.setScene(scene);

        //con esto mostramos la aplicacion en pantalla
        stage.show();
    }

    public static void main(String[] args) {
        //este es el metodo que arranca toda la ejecucion de JavaFX
        launch();
    }
}