package com.equipo06.pacientes.service;

public class Validaciones {

    //Esta funcion revisa si alguno de los cuadritos de texto se quedo en blanco
    public static boolean camposVacios(String... campos){
        for(String campo : campos){
            // Si el campo es nulo o solo tiene espacios, avisamos que esta vacio
            if(campo==null || campo.trim().isEmpty()){
                return true;
            }
        }
        return false; // Si todos tienen algo escrito, todo bien
    }

    //aqui checamos que el nombre no sea muy cortito que tenga mas de 5 letras
    public static boolean nombreValido(String nombre){
        return nombre != null && nombre.trim().length() > 5;
    }

    //aqui validamos que la edad sea algo logico osea que no pongan -negativos o numeros muy grandes
    public static boolean edadValida(int edad){
        return edad > 0 && edad <= 120;
    }

    //checa que el telefono sea de 10 numeros
    public static boolean telefonoValido(String telefon){
        if (telefon == null){
            return false;
        }
        // primero vemos si tiene exactamente los 10 digitos
        if(telefon.length() != 10){
            return false;
        }
        //luego revisamos uno por uno que todos sean numeros y no letras
        for(int i = 0; i < telefon.length(); i++){
            char c = telefon.charAt(i);
            if(c < '0' || c > '9'){
                return false;
            }
        }
        return true; //si paso las dos pruebas, el telefono es valido
    }

    //checamos que la curp no este muy chiquita o corta
    public static boolean curpValida(String curp){
        return curp != null && curp.length() >= 10;
    }
}