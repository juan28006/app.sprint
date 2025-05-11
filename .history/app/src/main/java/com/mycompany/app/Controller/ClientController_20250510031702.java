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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Request.ClientRegistrationRequest;
import com.mycompany.app.Controller.Request.CreateReservationRequest;
import com.mycompany.app.Controller.Validator.ClientValidator;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;
import com.mycompany.app.service.Interface.ReservationService;

@RestController
@RequestMapping("/api/client")
public class ClientController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientValidator clientValidator;

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(
            @RequestBody ClientRegistrationRequest request) { // Cambio a @RequestBody
        try {
            clientValidator.validateForCreation(request);

            // 1. Registrar Persona
            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(request.getName());
            personDTO.setDocument(request.getDocument());
            personDTO.setCellphone(request.getCellphone());
            inventoryService.createPerson(personDTO);

            // 2. Registrar Usuario (Cliente)
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(request.getUsername());
            userDTO.setPassword(request.getPassword());
            userDTO.setPerson(personDTO); // ← Aquí está el cambio clave
            UserDTO createdUser = inventoryService.createCliente(userDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", createdUser.getId(),
                    "message", "Cliente registrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al registrar cliente",
                    "message", e.getMessage()));
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequest request) {
        try {
            // 1. Validar datos básicos
            if (request == null || request.getUser() == null || request.getMachinery() == null) {
                throw new Exception("Datos de reserva incompletos");
            }

            // 2. Obtener usuario autenticado
            UserDTO currentUser = inventoryService.getUserById(request.getUser().getId());
            if (currentUser == null) {
                throw new Exception("Usuario no encontrado");
            }

            // 3. Validar permisos
            inventoryService.validatePermission(currentUser, "Reservar_maquinaria");

            // 4. Crear DTO de reserva
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setUser(currentUser);
            reservationDTO.setMachinery(request.getMachinery());

            // 5. Crear reserva
            ReservationDTO createdReservation = inventoryService.createReservation(reservationDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "data", createdReservation,
                    "message", "Reserva creada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al crear reserva",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now()));
        }
    }

    @PutMapping("/updateReserva/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO updatedReservation = inventoryService.updateReservation(id, reservationDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", updatedReservation,
                    "message", "Reservation updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Error updating reservation",
                    "message", e.getMessage()));
        }
    }

    @DeleteMapping("reservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/available")
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

// @GetMapping
// public ResponseEntity<List<ReservationDTO>> getAllReservations() {
// try {
// List<ReservationDTO> reservations = reservationService.getAllReservations();
// return ResponseEntity.ok(reservations);
// } catch (Exception e) {
// return ResponseEntity.status(500).body(null);
// }
// }