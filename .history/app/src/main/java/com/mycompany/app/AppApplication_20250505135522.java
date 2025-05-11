package com.mycompany.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

    /*
     * orden de insercion en base dedatos
     * 1. Registar person con su tipo de usuario
     * → 2. Iniciar sesion
     * → 3. Inventario
     * → 4. Maquinaria
     * → 5. Tipo de Usuario (Cliente)
     * → 6. Usuario Cliente
     * → 7. Reserva
     * → 8. Orden
     * → 9. Factura.
     * solucionar:1. autenticated y manejode 0 y 1
     * 2.el update de inventory nno deja hace
     * Enviar notificacion al usuario de que no puede realizar operaciones
     * que no tiene asignadas al querer hacer alguna funcion
     * Crear un login para cada usuario
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
