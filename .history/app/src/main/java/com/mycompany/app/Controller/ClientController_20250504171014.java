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

    private UserDTO usuarioActual;

    // ==================== ENDPOINTS CON PERMISOS ====================
    @PostMapping("/reservas")
    public ResponseEntity<?> crearReserva(@RequestBody ReservationDTO reserva) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Reservar_maquinaria",
                () -> {
                    ReservationDTO nuevaReserva = inventoryService.createReservation(reserva);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Reserva ID: " + nuevaReserva.getId());
                });
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> verDisponibilidad() {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Consultar_disponibilidad",
                () -> ResponseEntity.ok(inventoryService.getAvailableMachinery()));
    }

    // ==================== MÉTODOS DE INTERFAZ ====================
    @Override
    public void mostrarMenuPrincipal(UserDTO usuario) {
        this.usuarioActual = usuario;
    }

    @PostMapping("/logout")
    @Override
    public void cerrarSesion() {
        usuarioActual = null;
    }
}
