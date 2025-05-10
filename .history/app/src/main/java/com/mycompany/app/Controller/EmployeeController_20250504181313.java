/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.dto.AccessRequestDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/empleado")
public class EmployeeController implements ControllerInterface {
    @Autowired
    private InventoryService inventoryService;
    private UserDTO usuarioActual;

    // Access Control
    @PostMapping("/access")
    public ResponseEntity<?> controlAccess(@RequestBody AccessRequestDTO accessDTO) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Controlar_acceso",
                () -> ResponseEntity.ok(inventoryService.controlAccess(accessDTO)));
    }

    // Machinery View
    @GetMapping("/machinery")
    public ResponseEntity<?> viewMachinery() {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Consultar_disponibilidad",
                () -> ResponseEntity.ok(inventoryService.getAllMachinery()));
    }

    // Session Management
    @Override
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody UserDTO credentials) {
        try {
            UserDTO user = inventoryService.login(credentials.getUsername(), credentials.getPassword());
            this.usuarioActual = user;
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Override
    public void cerrarSesion() {
        usuarioActual = null;
    }
}
