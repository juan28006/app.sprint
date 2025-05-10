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
import org.springframework.web.bind.annotation.PostMapping;

import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class ClientController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserDTO usuarioActual;

    // Reservations
    @@PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            // El usuario ahora viene del token JWT o sesión
            // reservationDTO.setUser(usuarioActual); <- Eliminar esta línea
            
            ReservationDTO createdReservation = inventoryService.createReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdReservation,
                            "message", "Reserva creada exitosamente",
                            "reservationId", createdReservation.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al crear reserva",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Available Machinery
    @GetMapping("/machinery/available")
    public ResponseEntity<?> getAvailableMachinery() {
        try {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", inventoryService.getAvailableMachinery(),
                    "message", "Maquinaria disponible obtenida exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al obtener maquinaria disponible",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // // Session Management
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
