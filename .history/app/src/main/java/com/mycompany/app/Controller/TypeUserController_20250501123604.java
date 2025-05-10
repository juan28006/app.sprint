package com.mycompany.app.Controller;

import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.service.Interface.TypeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/type-users")

public class TypeUserController {
    @Autowired
    private TypeUserService typeUserService;

    @GetMapping
    public ResponseEntity<List<TypeUserDTO>> getAllTypeUsers() {
        try {
            List<TypeUserDTO> typeUsers = typeUserService.getAllTypeUsers();
            return ResponseEntity.ok(typeUsers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeUserById(@PathVariable Long id) {
        try {
            TypeUserDTO typeUserDTO = typeUserService.getTypeUserById(id);
            return ResponseEntity.ok(typeUserDTO);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTypeUser(@RequestBody TypeUserDTO typeUserDTO) {
        try {
            TypeUserDTO createdTypeUser = typeUserService.createTypeUser(typeUserDTO);
            return ResponseEntity.ok(createdTypeUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTypeUser(@PathVariable Long id,
            @RequestBody TypeUserDTO typeUserDTO) {
        try {
            TypeUserDTO updatedTypeUser = typeUserService.updateTypeUser(id, typeUserDTO);
            return ResponseEntity.ok(updatedTypeUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTypeUser(@PathVariable Long id) {
        try {
            typeUserService.deleteTypeUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}