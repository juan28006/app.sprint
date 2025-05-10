/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserDTO usuarioActual;

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(
            @RequestParam String name,
            @RequestParam Long document,
            @RequestParam Long cellphone,
            @RequestParam String username,
            @RequestParam String password) {

        try {
            // 1. Registrar Persona
            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(name);
            personDTO.setDocument(document);
            personDTO.setCellphone(cellphone);
            inventoryService.createPerson(personDTO);

            // 2. Registrar Usuario (Admin)
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setPassword(password); // Debe encriptarse
            userDTO.setTypeUser(new TypeUserDTO("admin", "Gestionar_inventario,Generar_reportes"));

            UserDTO createdUser = inventoryService.createUser(userDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", createdUser.getId(),
                    "message", "Administrador registrado exitosamente"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al registrar admin",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            UserDTO user = inventoryService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Usuario no encontrado",
                                "message", "No se encontró usuario con ID: " + id,
                                "timestamp", LocalDateTime.now()));
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", user,
                    "message", "Usuario obtenido exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al obtener usuario",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Inventory Management
    @PostMapping("/inventory")
    public ResponseEntity<?> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdInventory,
                            "message", "Inventario creado exitosamente",
                            "inventoryId", createdInventory.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al crear inventario",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Order Management
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO createdOrder = inventoryService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdOrder,
                            "message", "Orden creada exitosamente",
                            "orderId", createdOrder.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al crear orden",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Report Management
    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@RequestBody ReportDTO reportDTO) {
        try {
            ReportDTO createdReport = inventoryService.createReport(reportDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdReport,
                            "message", "Reporte generado exitosamente",
                            "reportId", createdReport.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al generar reporte",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Session Management
    // @Override
    // @PostMapping("/login")
    // public ResponseEntity<?> iniciarSesion(@RequestBody UserDTO credentials) {
    // try {
    // UserDTO user = inventoryService.login(credentials.getUsername(),
    // credentials.getPassword());
    // this.usuarioActual = user;
    // return ResponseEntity.ok(Map.of(
    // "success", true,
    // "data", user,
    // "message", "Inicio de sesión exitoso"));
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    // .body(Map.of(
    // "error", "Error en inicio de sesión",
    // "message", e.getMessage(),
    // "timestamp", LocalDateTime.now()));
    // }
    // }

    @Override
    public void session() throws Exception {

    }
}
