/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycompany.app.dto.LoginRequestDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

public class LoginController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private AdminController adminController;
    @Autowired
    private EmployeeController employeeController;
    @Autowired
    private ClientController clientController;

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequestDTO credenciales) {
        try {
            UserDTO usuario = inventoryService.login(credenciales.getUsername(), credenciales.getPassword());
            String rol = usuario.getTypeUser().getType().toLowerCase();

            switch (rol) {
                case "admin":
                    adminController.mostrarMenuPrincipal(usuario);
                    break;
                case "empleado":
                    employeeController.mostrarMenuPrincipal(usuario);
                    break;
                case "cliente":
                    clientController.mostrarMenuPrincipal(usuario);
                    break;
                default:
                    throw new Exception("Rol no válido");
            }
            return ResponseEntity.ok("Bienvenido " + rol);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

}
