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

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            // Validar que el usuario tiene permiso para reservar
            UserDTO currentUser = inventoryService.getCurrentUser(); // Método a implementar en InventoryService
            inventoryService.validatePermission(currentUser, "Reservar_maquinaria");

            // Verificar disponibilidad de maquinaria
            if (!inventoryService.isMachineryAvailable(reservationDTO.getMachinery().getId())) {
                throw new Exception("La maquinaria no está disponible");
            }

            // Asignar usuario a la reserva
            reservationDTO.setUser(currentUser);

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

    @Override
    public void session() throws Exception {
        // Implementación para verificar sesión
    }
}
