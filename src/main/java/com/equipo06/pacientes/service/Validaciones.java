package com.equipo06.pacientes.service;

public class Validaciones {
    public static boolean camposVacios(String... campos){
        for(String campo : campos){
            if(campo==null || campo.trim().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public static boolean nombreValido(String nombre){
        return nombre != null && nombre.trim().length() > 5;
    }
    public static boolean edadValida(int edad){
        return edad >=0 && edad <= 120;
    }
    public static boolean telefonoValido(String telefon){
        if (telefon == null){
            return false;
        }if(telefon.length() != 10){
            return false;
        }
        for(int i = 0; i<telefon.length(); i++){
            char c =telefon.charAt(i);
            if(c<'0' || c>'9'){
                return false;
            }
        }
        return true;
    }
    public static boolean curpValida(String curp){
        return curp !=null && curp.length() >= 10;
    }
}
