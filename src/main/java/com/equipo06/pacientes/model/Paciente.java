package com.equipo06.pacientes.model;
public class Paciente {
    //estas son las variables para guardar la info de cada paciente
    private String curp;
    private String nombre;
    private int edad;
    private String telefono;
    private String alergias;
    private String status; // Aqui guardamos si esta Activo o Inactivo

    //este es el constructor, nos sirve para crear al paciente con todos sus datos de un jalon
    public Paciente(String curp, String nombre, int edad, String telefono, String alergias, String status){
        this.curp = curp;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.alergias= alergias;
        this.status= status;
    }

    // De aqui para abajo son los Getters y Setters
    //los Get sirven para "obtener" el dato y los Set para "poner" o cambiar el dato

    public String getCurp(){
        return curp;
    }
    public void setCurp(String curp){
        this.curp = curp;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public int getEdad(){
        return edad;
    }
    public void setEdad(int edad){
        this.edad = edad;
    }
    public String getTelefono(){
        return telefono;
    }
    public void setTelefono(String telefono){
        this.telefono = telefono;
    }
    public String getAlergias(){
        return alergias;
    }
    public void setAlergias(String alergias){
        this.alergias = alergias;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}