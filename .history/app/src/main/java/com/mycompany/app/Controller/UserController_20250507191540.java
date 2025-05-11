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
import com.mycompany.app.dto.PersonDTO;
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
            // 1. Validar que el ID no sea nulo o inválido
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "ID inválido",
                        "message", "El ID de usuario debe ser un número positivo"));
            }

            // 2. Obtener el usuario para verificar existencia y obtener datos de la persona
            UserDTO userDTO = userService.getUserById(id);
            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "Usuario no encontrado",
                        "message", "No existe usuario con ID: " + id));
            }

            // 3. Verificar si el usuario tiene una persona asociada
            if (userDTO.getPerson() == null || userDTO.getPerson().getId() == null) {
                // Solo eliminar el usuario si no tiene persona asociada
                userService.deleteUser(id);
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Usuario eliminado exitosamente (sin persona asociada)",
                        "userId", id));
            }

            // 4. Eliminar usuario y persona asociada
            userService.deleteUser(id);

            // Eliminar la persona (esto debería manejarse en el servicio)
            PersonDTO personDTO = userDTO.getPerson();
            userService.deletePerson(personDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Usuario y persona asociada eliminados exitosamente",
                    "userId", id,
                    "personId", personDTO.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error al eliminar usuario",
                    "message", e.getMessage(),
                    "details", "Ocurrió un error durante la eliminación. Verifique que no haya datos dependientes."));
        }
    }
}