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
    private void btnEliminarAction() {
        // Primero, revisamos qué paciente esta seleccionado en la tabla
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        // Si el usuario si selecciono a alguien (si no es nulo)
        if (seleccionado != null) {
            // Creamos una caja de mensaje para confirmar (para que no se borre por accidente)
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Inactivación"); // Titulo de la venta
            confirmacion.setHeaderText(null); // Esto es para que no se vea doble texto arriba
            // El mensaje que le pregunta al usuario si de verdad lo quiere inactivar
            confirmacion.setContentText("¿Deseas marcar como INACTIVO a " + seleccionado.getNombre() + "?");
            // Si el usuario le da clic al boton de "Aceptar" o "OK"
            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                // Le pedimos al repositorio que lo marque como INACTIVO (borrado logico)
                // No lo quitamos de la lista, solo le cambiamos su etiqueta de estatus
                repository.eliminarPaciente(seleccionado);
                // Le decimos a la tabla que se actualice para que se vea el cambio de texto
                tblPacientes.refresh();
                // Actualizamos los numeritos de arriba (Total, Activos, Inactivos)
                actualizarResumen();
                // Mostramos un aviso final de que todo salio bien
                mostrarAlerta("Éxito", "El paciente ahora está INACTIVO");
            }

        } else {
            // Si el usuario pico el boton pero no eligio a nadie en la tabla, le avisamos
            mostrarAlerta("Atención", "Selecciona un paciente para eliminar.");
        }
    }
    @FXML
    private void btnGuardarAction() {
        String curp = txtCurp.getText();
        String nombre = txtNombre.getText();
        String edadStr = txtEdad.getText();
        String tel = txtTelefono.getText();
        String alergias = txtAlergias.getText();

        // validar los campos que estan vacios,para que sea obligatorio que todos los campos esten llenos
        if (Validaciones.camposVacios(curp, nombre, edadStr, tel, alergias)) {
            mostrarAlerta("Error", "Todos los campos (incluyendo alergias) son obligatorios");
            return;
        }

        // validar el nombre de mas de 5 letras
        if (!Validaciones.nombreValido(nombre)) {
            mostrarAlerta("Error", "El nombre debe tener mas de 5 letras");
            return;
        }

        // validar el numero de telefono que sea exactamente de 10 numeros
        if (!Validaciones.telefonoValido(tel)) {
            mostrarAlerta("Error", "El telefono debe tener 10 digitos numericos");
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            if (!Validaciones.edadValida(edad)) {
                mostrarAlerta("Error", "La edad debe estar entre 0 y 120");
                return;
            }
            if (modoEdicion) {
                // estamos editando los datos de nuestro paciente, creamos el objeto y actualizamos
                Paciente pEditado = new Paciente(curp, nombre, edad, tel, alergias, "ACTIVO");
                repository.actualizarPaciente(pEditado);
                mostrarAlerta("Exito", "Paciente actualizado correctamente");
            } else {
                // agregamos o registramos a un nuevo paciente, intentamos agregarlo
                Paciente nuevo = new Paciente(curp, nombre, edad, tel, alergias, "ACTIVO");
                boolean exito = repository.agregarPaciente(nuevo);

                if (exito) {
                    mostrarAlerta("Exito", "Paciente registrado correctamente");
                } else {
                    mostrarAlerta("Error", "La CURP ya existe en el sistema");
                    return;
                }
            }

            // lo ocupamos para limpiar la pantalla y refrescando la tabla
            limpiarFormulario();
            tblPacientes.refresh();
            actualizarResumen();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La edad debe ser un numero entero");
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