package com.mycompany.app.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.Interface.UserService;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado con ID: " + id);
            }
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar usuario: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            userValidator.validateForCreation(userDTO);
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario '" + createdUser.getUsername() + "' creado exitosamente con ID: "
                            + createdUser.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        try {
            // 1. Obtener el usuario existente
            UserDTO existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Usuario no encontrado",
                                "message", "No existe usuario con ID: " + id));
            }

            // 2. Actualización parcial (solo campos no nulos)
            boolean hasUpdates = false;

            if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
                existingUser.setUsername(userDTO.getUsername());
                hasUpdates = true;
            }

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userDTO.getPassword());
                hasUpdates = true;
            }

            if (userDTO.isAuthenticated() != existingUser.isAuthenticated()) {
                existingUser.setAuthenticated(userDTO.isAuthenticated());
                hasUpdates = true;
            }

            // 3. Validar si hubo cambios
            if (!hasUpdates) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "No se realizaron cambios",
                        "userId", id));
            }

            // 4. Aplicar actualización
            UserDTO updatedUser = userService.updateUser(id, existingUser);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Usuario actualizado exitosamente",
                    "userId", updatedUser.getId(),
                    "username", updatedUser.getUsername(),
                    "authenticated", updatedUser.isAuthenticated()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al actualizar usuario",
                    "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            // 1. Get the user first to validate
            UserDTO user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Usuario no encontrado",
                                "message", "No existe usuario con ID: " + id));
            }

            // 2. Check if user has any dependent data (orders, reports, etc.)
            // You would need to implement these checks in your services
            boolean hasDependencies = userService.hasUserDependencies(id);
            if (hasDependencies) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "error", "No se puede eliminar usuario",
                                "message", "El usuario tiene datos asociados (órdenes, facturas, etc.)"));
            }

            // 3. Delete the user (which will handle person deletion if appropriate)
            userService.deleteUser(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Usuario y datos asociados eliminados exitosamente",
                    "userId", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al eliminar usuario",
                    "message", e.getMessage()));
        }
    }
}