package com.mycompany.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {
    public static void main(String[] args) {
        /*
         * @Autowired:Inyeccion de depndencias;Encuentra el objeto que necesito y
         * automáticamente asígnalo aquí
         * ejemplo:un @Service con un @Repository).
         * 
         * @Component (@Component public class LoggerUtil)Transforma una clase común en
         * un "bean" (objeto gestionado por Spring)
         * Permite que Spring lo detecte, lo instancie y lo inyecte donde sea necesario.
         * 
         * @Service Lógica de negocio. @Service public class UserService
         * 
         * @Repository Acceso a datos (DAO). @Repository public class UserRepository
         * 
         * @Controller: En clases que manejan peticiones web (REST o páginas).
         * Controladores MVC
         * 
         * @RequestMapping:Mapea una URL(ruta de acceso web),acción ejecutar (navegador,
         * API, etc.)
         */
        SpringApplication.run(AppApplication.class, args);

    }

}
