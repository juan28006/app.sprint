/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.Interface.InventoryS;

public class LoginController {
    @Autowired
    private InventoryS inventoryService;

    @Autowired
    private AdminController adminController;

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private ClientController clientController;

    @Override
    public void iniciarSesion() throws Exception {
        boolean mantenerSesion = true;

        while (mantenerSesion) {
            Utils.limpiarConsola();
            System.out.println("=== SISTEMA GIMNASIO ===");
            String usuario = Utils.leerLinea("Usuario: ");
            String contrasena = Utils.leerLinea("Contraseña: ");

            try {
                UserDTO usuarioAutenticado = inventoryService.login(usuario, contrasena);
                redirigirSegunRol(usuarioAutenticado);
                mantenerSesion = false;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                Utils.leerLinea("Presione enter para continuar...");
            }
        }
    }

    private void redirigirSegunRol(UserDTO usuario) throws Exception {
        String rol = usuario.getTypeUser().getType().toLowerCase();

        switch (rol) {
            case "admin":
                adminController.mostrarMenuPrincipal();
                break;
            case "empleado":
                employeeController.mostrarMenuPrincipal();
                break;
            case "cliente":
                clientController.mostrarMenuPrincipal();
                break;
            default:
                throw new Exception("Rol no reconocido");
        }
    }

    @Override
    public void mostrarMenuPrincipal() {
        // No implementado aquí
    }

}
