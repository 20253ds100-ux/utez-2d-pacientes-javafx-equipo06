package com.equipo06.pacientes.controller;

import com.equipo06.pacientes.model.Paciente;
import com.equipo06.pacientes.service.PacienteRepository;
import com.equipo06.pacientes.service.Validaciones;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PacienteController {

    // Componentes de la Interfaz (deben coincidir con el FXML)
    @FXML private TableView<Paciente> tblPacientes;
    @FXML private TableColumn<Paciente, String> colCurp, colNombre, colTelefono, colStatus;
    @FXML private TextField txtCurp, txtNombre, txtEdad, txtTelefono, txtAlergias;
    @FXML private Label lblTotal, lblActivos, lblInactivos;

    private PacienteRepository repository = new PacienteRepository();

    @FXML
    public void initialize() {
        // 1. Cargar datos del archivo al iniciar
        repository.cargarArchivo();

        // 2. Configurar las columnas de la tabla
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 3. Vincular la lista a la tabla y actualizar resumen
        tblPacientes.setItems(repository.getListaPacientes());
        actualizarResumen();
    }
    @FXML
    private void btnEditarAction() {
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Cargamos los datos de la tabla a los campos de texto
            txtCurp.setText(seleccionado.getCurp());
            txtNombre.setText(seleccionado.getNombre());
            txtEdad.setText(String.valueOf(seleccionado.getEdad()));
            txtTelefono.setText(seleccionado.getTelefono());
            txtAlergias.setText(seleccionado.getAlergias());

            // El botón de Guardar ahora servirá para actualizar gracias al ID (CURP)
            mostrarAlerta("Modo Edición", "Modifica los datos y presiona 'Guardar' para actualizar.");
        } else {
            mostrarAlerta("Atención", "Selecciona un paciente para editar.");
        }
    }

    @FXML
    private void btnEliminarAction() {
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Creamos una alerta de confirmación (Punto B de la rúbrica)
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro de que deseas eliminar a " + seleccionado.getNombre() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                repository.getListaPacientes().remove(seleccionado);
                repository.guardarArchivo();
                actualizarResumen();
            }
        } else {
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

        // VALIDACIONES (Usando la clase de Monse)
        if (Validaciones.camposVacios(curp, nombre, edadStr, tel)) {
            mostrarAlerta("Campos vacíos", "Por favor rellena todos los campos obligatorios.");
            return;
        }

        if (!Validaciones.nombreValido(nombre)) {
            mostrarAlerta("Nombre inválido", "El nombre debe tener más de 5 caracteres.");
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            if (!Validaciones.edadValida(edad)) {
                mostrarAlerta("Edad inválida", "La edad debe estar entre 0 y 120.");
                return;
            }

            Paciente nuevo = new Paciente(curp, nombre, edad, tel, alergias, "ACTIVO");

            if (repository.agregarPaciente(nuevo)) {
                limpiarFormulario();
                actualizarResumen();
            } else {
                mostrarAlerta("Duplicado", "Ya existe un paciente con ese CURP.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La edad debe ser un número.");
        }
    }

    @FXML
    private void btnCambiarEstatusAction() {
        Paciente seleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            repository.cambiarEstatus(seleccionado);
            tblPacientes.refresh();
            actualizarResumen();
        } else {
            mostrarAlerta("Atención", "Selecciona un paciente de la tabla.");
        }
    }

    private void actualizarResumen() {
        int total = repository.getListaPacientes().size();
        int activos = (int) repository.getListaPacientes().stream()
                .filter(p -> p.getStatus().equalsIgnoreCase("ACTIVO")).count();

        lblTotal.setText("Total: " + total);
        lblActivos.setText("Activos: " + activos);
        lblInactivos.setText("Inactivos: " + (total - activos));
    }

    private void limpiarFormulario() {
        txtCurp.clear();
        txtNombre.clear();
        txtEdad.clear();
        txtTelefono.clear();
        txtAlergias.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}