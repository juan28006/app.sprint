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
     * eliminar un usuario se elimine con la persona registrada y los datos
     * asociados a este usuario
     * solucionar:1. autenticated y false 0 y true 1
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
