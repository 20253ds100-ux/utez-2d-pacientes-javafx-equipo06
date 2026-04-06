package com.equipo06.pacientes.service;

import com.equipo06.pacientes.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class PacienteRepository {
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private final String ARCHIVO = "pacientes.csv";
    public ObservableList<Paciente> getListaPacientes() {
        return listaPacientes;
    }
    public void cargarArchivo() {
        listaPacientes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 6) {
                    Paciente paciente = new Paciente(datos[0], datos[1], Integer.parseInt(datos[2]), datos[3], datos[4], datos[5]);
                    listaPacientes.add(paciente);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        }
    }
    public void guardarArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Paciente p : listaPacientes) {
                pw.println(p.getCurp() + "," + p.getNombre() + "," + p.getEdad() + "," + p.getTelefono() + "," + p.getAlergias() + "," + p.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo");
        }
    }
    public boolean agregarPaciente(Paciente paciente) {
        for (Paciente p : listaPacientes) {
            if (p.getCurp().equalsIgnoreCase(paciente.getCurp())) {
                return false; // ya existe
            }
        }
        listaPacientes.add(paciente);
        guardarArchivo();
        return true;
    }
    public void actualizarPaciente(Paciente pacienteActualizado) {
        for (int i =0; i < listaPacientes.size(); i++) {
            if (listaPacientes.get(i).getCurp().equalsIgnoreCase(pacienteActualizado.getCurp())) {
                listaPacientes.set(i, pacienteActualizado);
                break;
            }
        }guardarArchivo();
    }
    public void cambiarEstatus(Paciente paciente) {
        if (paciente.getStatus().equalsIgnoreCase("ACTIVO")) {
            paciente.setStatus("INACTIVO");
        } else {
            paciente.setStatus("ACTIVO");
        }
        guardarArchivo();
    }
    public void eliminarPaciente(Paciente paciente) {
        paciente.setStatus("INACTIVO");
        guardarArchivo();
    }
}