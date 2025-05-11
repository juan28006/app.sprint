package com.mycompany.app.Controller;

import java.util.List;

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

import com.mycompany.app.Controller.Validator.TypeUserValidator;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.service.Interface.TypeUserService;

@RestController
@RequestMapping("/api/type-users")

public class TypeUserController {

    @Autowired
    private TypeUserService typeUserService;

    @Autowired
    TypeUserValidator validator;

    @GetMapping
    public ResponseEntity<?> getAllTypeUsers() {
        try {
            List<TypeUserDTO> typeUsers = typeUserService.getAllTypeUsers();
            if (typeUsers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron tipos de usuario registrados");
            }
            return ResponseEntity.ok(typeUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar los tipos de usuario: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeUserById(@PathVariable Long id) {
        try {
            TypeUserDTO typeUserDTO = typeUserService.getTypeUserById(id);
            if (typeUserDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tipo de usuario con ID " + id + " no encontrado");
            }
            return ResponseEntity.ok(typeUserDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el tipo de usuario: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTypeUser(@RequestBody TypeUserDTO typeUserDTO) {
        try {
            validator.validate(typeUserDTO);
            TypeUserDTO createdTypeUser = typeUserService.createTypeUser(typeUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Tipo de usuario '" + createdTypeUser.getType() + "' creado exitosamente con ID "
                            + createdTypeUser.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo crear el tipo de usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTypeUser(@PathVariable Long id,
            @RequestBody TypeUserDTO typeUserDTO) {
        try {
            TypeUserDTO updatedTypeUser = typeUserService.updateTypeUser(id, typeUserDTO);
            if (updatedTypeUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el tipo de usuario con ID " + id + " para actualizar");
            }
            return ResponseEntity.ok(
                    "Tipo de usuario '" + updatedTypeUser.getType() + "' (ID: " + id + ") actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar el tipo de usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTypeUser(@PathVariable Long id) {
        try {
            typeUserService.deleteTypeUser(id);
            return ResponseEntity.ok("Tipo de usuario con ID " + id + " eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al eliminar tipo de usuario: " + e.getMessage());
        }
}