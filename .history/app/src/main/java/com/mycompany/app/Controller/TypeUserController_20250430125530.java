package com.mycompany.app.Controller;

import com.mycompany.app.Dto.TypeUserDTO;
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
    public List<TypeUserDTO> getAllTypeUsers() throws Exception {
        return typeUserService.getAllTypeUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeUserDTO> getTypeUserById(@PathVariable Long id) throws Exception {
        TypeUserDTO typeUserDTO = typeUserService.getTypeUserById(id);
        return ResponseEntity.ok(typeUserDTO);
    }

    @PostMapping
    public ResponseEntity<TypeUserDTO> createTypeUser(@RequestBody TypeUserDTO typeUserDTO) throws Exception {
        TypeUserDTO createdTypeUser = typeUserService.createTypeUser(typeUserDTO);
        return ResponseEntity.ok(createdTypeUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeUserDTO> updateTypeUser(@PathVariable Long id,
            @RequestBody TypeUserDTO typeUserDTO) throws Exception {
        TypeUserDTO updatedTypeUser = typeUserService.updateTypeUser(id, typeUserDTO);
        return ResponseEntity.ok(updatedTypeUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeUser(@PathVariable Long id) throws Exception {
        typeUserService.deleteTypeUser(id);
        return ResponseEntity.noContent().build();
    }
}