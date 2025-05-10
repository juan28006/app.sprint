package com.mycompany.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

    /*
     * 1. Tipo de Usuario (Admin) → 2. Usuario Admin → 3. Inventario → 4. Maquinaria
     * →
     * 5. Tipo de Usuario (Cliente) →
     * 6. Usuario Cliente → 7. Reserva →
     * 8. Orden → 9. Factura.
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
