package com.mycompany.app.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
            validator.validate(typeUserDTO);
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