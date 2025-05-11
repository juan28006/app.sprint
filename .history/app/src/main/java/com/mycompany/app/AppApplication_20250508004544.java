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
     * cambiar report a reportinvoicescontroller de admincontroller
     * 
     * 
     * ""en los metodos update de cada controller implementar condicionales que me
     * permitan
     * actualizar de a un campo sin necesidad de actualizar todos los campos como en
     * el
     * (update del usercontroller) """
     * 
     * 
     * eliminar un usuario se elimine con la persona registrada y los datos
     * asociados a este usuario
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
