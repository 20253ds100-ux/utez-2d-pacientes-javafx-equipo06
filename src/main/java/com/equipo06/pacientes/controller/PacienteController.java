package com.equipo06.pacientes.controller;
import com.equipo06.pacientes.model.Paciente;
import com.equipo06.pacientes.service.PacienteRepository;
import com.equipo06.pacientes.service.Validaciones;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PacienteController {

    // Aqui ponemos las cosas de la pantalla que el FXML debe conocer
    @FXML private TableView<Paciente> tblPacientes; // Esta es la tablita donde se ven los pacientes
    @FXML private TableColumn<Paciente, String> colCurp, colNombre, colTelefono, colStatus; //columnas de texto
    @FXML private TextField txtCurp, txtNombre, txtEdad, txtTelefono, txtAlergias; // Los cuadritos para escribir datos
    @FXML private Label lblTotal, lblActivos, lblInactivos; //los textos que cambian para el resumen
    @FXML private TableColumn<Paciente, Integer> colEdad; // columna para numeros (edad)
    @FXML private TableColumn<Paciente, String> colAlergias; //columna para las alergias

    private boolean modoEdicion = false; // esta bandera nos dice si estamos editando o creando uno nuevo
    private PacienteRepository repository = new PacienteRepository(); //Llamamos al archivo que guarda los datos

    @FXML
    public void initialize(){ // Esto se ejecuta solito en cuanto abre la ventana
        repository.cargarArchivo(); //leemos los pacientes que ya estan guardados en el CSV

        //aqui le decimos a cada columna que dato del objeto Paciente debe mostrar
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colAlergias.setCellValueFactory(new PropertyValueFactory<>("alergias"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblPacientes.setItems(repository.getListaPacientes()); //esto es para que meetamos la lista de pacientes a la tabla
        actualizarResumen(); // Calculamos los totales del inicio
    }

    @FXML
    private void btnEditarAction() { // lo que pasa al picarle a Editar
        Paciente seleccionado=tblPacientes.getSelectionModel().getSelectedItem(); //aqui vemos cual fila eligio el usuario
        if (seleccionado != null) { // Si eligio a alguien
            //pasamos los datos de la tabla a los cuadritos de texto
            txtCurp.setText(seleccionado.getCurp());
            txtNombre.setText(seleccionado.getNombre());
            txtEdad.setText(String.valueOf(seleccionado.getEdad()));
            txtTelefono.setText(seleccionado.getTelefono());
            txtAlergias.setText(seleccionado.getAlergias());

            txtCurp.setEditable(false); //no dejamos que cambien el curp porque es unica
            modoEdicion = true; //aqui avisamos que estamos en modo editar
            mostrarAlerta("Modo Edicion", "Modifica los datos y presiona 'Guardar'.");
        } else {
            mostrarAlerta("Atencion", "Selecciona un paciente para editar.");
        }
    }

    @FXML
    private void btnEliminarAction() { //lo que pasa al picarle a Eliminar
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado !=null) {
            //aqui ponemos un aviso de confirmacion para que no borren por error
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminacion");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("Estas seguro de que deseas eliminar a " + seleccionado.getNombre() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) { //si el usuario le da a que si
                repository.getListaPacientes().remove(seleccionado); // Lo quitamos de la lista
                repository.guardarArchivo(); // Guardamos el cambio en el archivo
                actualizarResumen(); // Recalculamos los totales
            }
        } else {
            mostrarAlerta("Atencion", "Selecciona un paciente para eliminar.");
        }
    }

    @FXML
    private void btnGuardarAction() { // El boton mas importante: Guardar
        try {
            int edad = Integer.parseInt(txtEdad.getText()); // Convertimos el texto de edad a numero
            // Creamos un objeto temporal con lo que hay en los cuadritos
            Paciente p = new Paciente(txtCurp.getText(), txtNombre.getText(), edad, txtTelefono.getText(), txtAlergias.getText(), "ACTIVO");

            if (modoEdicion) { // Si la bandera de edicion esta prendida...
                repository.actualizarPaciente(p); // Usamos la funcion de actualizar
                modoEdicion =false; // Apagamos la bandera
                txtCurp.setEditable(true); // Dejamos que la CURP se pueda escribir otra vez
                limpiarFormulario(); // Borramos todo
                actualizarResumen(); // Actualizamos numeritos
                tblPacientes.refresh(); // Refrescamos la tabla para que se vea el cambio
                mostrarAlerta("Exito", "Paciente actualizado correctamente.");
            } else { // Si es un paciente nuevo...
                if (repository.agregarPaciente(p)) { // Intentamos agregarlo
                    limpiarFormulario();
                    actualizarResumen();
                    mostrarAlerta("Exito", "Paciente registrado.");
                } else {
                    mostrarAlerta("Duplicado", "Ya existe un paciente con ese CURP.");
                }
            }
        } catch (NumberFormatException e) { // Si pusieron letras en la edad, sale este error
            mostrarAlerta("Error", "La edad debe ser un numero.");
        }
    }

    @FXML
    private void btnCambiarEstatusAction() { // para poner Activo o Inactivo
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado !=null) {
            repository.cambiarEstatus(seleccionado); // Llamamos a la logica del servicio
            tblPacientes.refresh(); // actualizamos la vista
            actualizarResumen(); // actualizamos el conteo de activos/inactivos
        } else {
            mostrarAlerta("Atencion", "Selecciona un paciente de la tabla.");
        }
    }

    private void actualizarResumen() { // funcion para contar cuantos hay de cada uno
        int total = repository.getListaPacientes().size(); // Cuantos hay en total
        int activos = (int) repository.getListaPacientes().stream()
                .filter(p -> p.getStatus().equalsIgnoreCase("ACTIVO")).count(); // Contamos solo los activos

        // ponemos los resultados en las etiquetas de la pantalla
        lblTotal.setText("Total: " + total);
        lblActivos.setText("Activos: " + activos);
        lblInactivos.setText("Inactivos: " + (total - activos));
    }

    @FXML
    public void limpiarFormulario() { // este se encarga de resetear
        txtCurp.clear();
        txtNombre.clear();
        txtEdad.clear();
        txtTelefono.clear();
        txtAlergias.clear();
        txtCurp.setEditable(true);
        modoEdicion = false; // Resetamos todo a como estaba al inicio
    }

    private void mostrarAlerta(String titulo, String mensaje) { // sirve para sacar los mensajes de aviso
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}