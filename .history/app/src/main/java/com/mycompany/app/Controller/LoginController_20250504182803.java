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

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
            }
            return ResponseEntity.ok("Bienvenido " + rol);
        }catch(

    Exception e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
    }
}
