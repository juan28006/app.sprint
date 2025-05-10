/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class ClientController {

    @Autowired
    private InventoryService inventoryService;

    // Reservar_maquinaria
    @PostMapping("/reservas")
    public ResponseEntity<?> crearReserva(@RequestBody ReservationDTO reservationDTO) {
        try {
            return ResponseEntity.ok(inventoryService.createReservation(reservationDTO, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar_disponibilidad
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> verDisponibilidad() {
        try {
            return ResponseEntity.ok(inventoryService.getAvailableMachinery());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
