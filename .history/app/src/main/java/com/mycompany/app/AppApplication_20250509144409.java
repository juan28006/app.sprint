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
     * → 4. Orden
     * → 5. Factura.(si orden esta aprobadaS)
     * -> 6. maquinaria (si factura esta pagada)
     * → 7. Reserva
     * 
     * (smtp enviar correo electronico)
     * 
     * eliminar un usuario se elimine con la persona registrada y los datos
     * asociados a este usuario
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
