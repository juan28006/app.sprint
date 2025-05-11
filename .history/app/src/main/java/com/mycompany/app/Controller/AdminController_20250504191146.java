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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements ControllerInterface {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private LoginController loginController;

    @Autowired
    private UserDTO usuarioActual;

    // User Management
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Gestionar_usuarios",
                () -> {
                    UserDTO createdUser = inventoryService.createUser(userDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
                });
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Consultar_usuarios",
                () -> ResponseEntity.ok(inventoryService.getUserById(id)));
    }

    // Inventory Management
    @PostMapping("/inventory")
    public ResponseEntity<?> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Gestionar_inventario",
                () -> ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createInventory(inventoryDTO)));
    }

    // Order Management
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Orden_maquinaria",
                () -> ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createOrder(orderDTO)));
    }

    // Report Management
    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@RequestBody ReportDTO reportDTO) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Generar_informes",
                () -> ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createReport(reportDTO)));
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

    @Override
    @PostMapping("/logout")
    public ResponseEntity<?> cerrarSesion() {
        this.usuarioActual = null;
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }
}
