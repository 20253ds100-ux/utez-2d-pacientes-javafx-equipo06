# utez-2d-pacientes-javafx-equipo06
Tarea integradora Allison Maldonado y Monserrath Anzurez - Consultorio

Sistema de Directorio de Pacientes - JavaFX
Descripción del proyecto
Aplicación de escritorio desarrollada en JavaFX que permite administrar un directorio de pacientes de un consultorio médico.

El sistema implementa operaciones CRUD (Crear, Leer, Actualizar y Eliminar) con persistencia en archivo local, permitiendo
conservar la información entre ejecuciones.

Integrantes del equipo:
Allison Maldonado
Monserrath Anzurez

Herramientas de desarrollo:
Java
JavaFX
IntelliJ IDEA
Git & GitHub

Funcionalidades:
CRUD completo
-Agregar paciente
-Visualizar pacientes
-Actualizar información
-Eliminar

Persistencia:
Los datos se almacenan en un archivo .csv
Al iniciar la aplicación, se cargan automáticamente

Validaciones:
-Nombre mínimo 5 caracteres
-Edad entre 0 y 120
-Teléfono de 10 dígitos
-CURP única (sin duplicados)
-No se permiten campos vacíos

Estatus de paciente:
ACTIVO o INACTIVO

Lo que se muestra en pantalla:
-Total de pacientes
-Pacientes activos
-Pacientes inactivos

Interfaz:
La aplicación cuenta con:
-Tabla para visualizar pacientes
-Formulario de registro

Botones:
-Nuevo
-Editar
-Cambiar estatus
-Eliminar
-Recargar

Estructura de nuestro proyecto:
controllers/
models/
services/
repositories/
views/
data/