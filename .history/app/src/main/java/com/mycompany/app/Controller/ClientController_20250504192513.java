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
    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) throws Exception {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Reservar_maquinaria",
                () -> {
                    reservationDTO.setUser(usuarioActual);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(inventoryService.createReservation(reservationDTO, usuarioActual.getId()));
                });
    }

    // Available Machinery
    @GetMapping("/machinery/available")
    public ResponseEntity<?> getAvailableMachinery() throws Exception {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Consultar_disponibilidad",
                () -> ResponseEntity.ok(inventoryService.getAvailableMachinery()));
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
