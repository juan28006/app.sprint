package com.mycompany.app.Controller;

import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) throws Exception {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserDTO> createAdmin(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO createdAdmin = userService.createAdmin(userDTO);
        return ResponseEntity.ok(createdAdmin);
    }

    @PostMapping("/create-empleado")
    public ResponseEntity<UserDTO> createEmpleado(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO createdEmployee = userService.createEmpleado(userDTO);
        return ResponseEntity.ok(createdEmployee);
    }

    @PostMapping("/create-cliente")
    public ResponseEntity<UserDTO> createCliente(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO createdClient = userService.createCliente(userDTO);
        return ResponseEntity.ok(createdClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
            @RequestBody UserDTO userDTO) throws Exception {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws Exception {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}