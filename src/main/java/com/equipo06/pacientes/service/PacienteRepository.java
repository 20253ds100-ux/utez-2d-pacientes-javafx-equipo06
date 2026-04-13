package com.equipo06.pacientes.service;

import com.equipo06.pacientes.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class PacienteRepository {
    //esta es una lista especial que JavaFX usa para actualizar la tabla solita
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    // Aqui le decimos como se llama nuestro archivo donde se guardara todo
    private final String ARCHIVO = "pacientes.csv";

    //funcion para que otros archivos puedan ver nuestra lista de pacientes
    public ObservableList<Paciente> getListaPacientes() {
        return listaPacientes;
    }

    //esta funcion lee el archivo CSV para traer los datos guardados
    public void cargarArchivo() {
        listaPacientes.clear(); //limpiamos la lista para no duplicar datos al cargar
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) { // Leemos linea por linea el archivo
                String[] datos = linea.split(","); // Separamos los datos por cada coma
                if (datos.length == 6) { // Si la linea tiene los 6 datos completos...
                    // Creamos el objeto paciente y lo metemos a la lista
                    Paciente paciente = new Paciente(datos[0], datos[1], Integer.parseInt(datos[2]), datos[3], datos[4], datos[5]);
                    listaPacientes.add(paciente);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, se creara uno nuevo");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        }
    }

    // Esta funcion agarra todo lo que hay en la lista y lo escribe en el CSV
    public void guardarArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Paciente p : listaPacientes) {
                //escribimos los datos separados por comas
                pw.println(p.getCurp() + "," + p.getNombre() + "," + p.getEdad() + "," + p.getTelefono() + "," + p.getAlergias() + "," + p.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo");
        }
    }

    //esta funcion es para meter un paciente nuevo si no esta repetido
    public boolean agregarPaciente(Paciente paciente) {
        for (Paciente p : listaPacientes) {
            if (p.getCurp().equalsIgnoreCase(paciente.getCurp())) {
                return false; //si el curp ya existe, decimos que no se pudo
            }
        }
        listaPacientes.add(paciente); //si es nuevo lo agregamos
        guardarArchivo(); //guardamos el cambio en el archivo de inmediato
        return true;
    }

    //funcion para reemplazar los datos de un paciente que ya existe
    public void actualizarPaciente(Paciente pacienteActualizado) {
        for (int i=0; i<listaPacientes.size(); i++) {
            // Buscamos al paciente por su CURP
            if (listaPacientes.get(i).getCurp().equalsIgnoreCase(pacienteActualizado.getCurp())) {
                listaPacientes.set(i, pacienteActualizado); // Cambiamos los datos viejos por los nuevos
                break;
            }
        }
        guardarArchivo(); // Guardamos los cambios
    }

    // Funcion para alternar entre Activo e Inactivo
    public void cambiarEstatus(Paciente paciente) {
        if (paciente.getStatus().equalsIgnoreCase("ACTIVO")) {
            paciente.setStatus("INACTIVO");
        } else {
            paciente.setStatus("ACTIVO");
        }
        guardarArchivo(); // Guardamos el nuevo estatus
    }

    // Funcion para el borrado logico (solo lo pone como inactivo)
    public void eliminarPaciente(Paciente paciente) {
        paciente.setStatus("INACTIVO");
        guardarArchivo();
    }
}