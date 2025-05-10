/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class LoginController {
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private AdminController adminController;

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private ClientController clientController;

    private Map<String, ControllerInterface> roles;

    public LoginController() {
        this.roles = new HashMap<>();
        roles.put("admin", adminController);
        roles.put("empleado", employeeController);
        roles.put("cliente", clientController);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO credentials) {
        try {
            // Validate credentials
            userValidator.validateForCreation(credentials);

            // Authenticate user
            UserDTO user = inventoryService.login(credentials.getUsername(), credentials.getPassword());

            // Get appropriate controller based on role
            ControllerInterface roleController = roles.get(user.getTypeUser().getType().toLowerCase());
            if (roleController == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Rol no válido");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
