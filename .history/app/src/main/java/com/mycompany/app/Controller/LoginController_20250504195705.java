/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycompany.app.dto.UserDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class LoginController {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO credentials) {
        try {
            // Autenticar usuario
            UserDTO user = inventoryService.login(credentials.getUsername(), credentials.getPassword());

            // Determinar el tipo de usuario
            String userType = user.getTypeUser().getType().toLowerCase();
            String dashboardRoute = "/api/" + userType;

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", user,
                    "message", "Inicio de sesión exitoso",
                    "userType", userType,
                    "dashboardRoute", dashboardRoute,
                    "token", "generar-token-jwt-aqui" // Opcional para autenticación stateless
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Error de autenticación",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }
}
