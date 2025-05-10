/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class LoginController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserDTO currentUser;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO credentials) {
        try {
            UserDTO user = inventoryService.login(credentials.getUsername(), credentials.getPassword());

            this.currentUser = user;
            // Verificar permisos básicos
            inventoryService.validatePermission(user, "Iniciar_sesion");

            // Determinar el tipo de usuario
            String userType = user.getTypeUser().getType().toLowerCase();
            String dashboardRoute = "/api/" + userType;

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", user,
                    "message", "Inicio de sesión exitoso",
                    "userType", userType,
                    "dashboardRoute", dashboardRoute,
                    "permissions", user.getTypeUser().getPermissions().split(",")));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Error de autenticación",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }
}
