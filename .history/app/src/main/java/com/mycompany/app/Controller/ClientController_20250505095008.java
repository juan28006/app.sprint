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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Request.ClientRegistrationRequest;
import com.mycompany.app.dto.ClientsDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

@RestController
@RequestMapping("/api/client")
public class ClientController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(
            @RequestBody ClientRegistrationRequest request) { // Cambio a @RequestBody
        try {
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

            // 3. Opcional: Crear ClientsDTO si necesitas más datos
            ClientsDTO clientDTO = new ClientsDTO();
            clientDTO.setUser(createdUser);
            clientDTO.setAddress(request.getAddress());

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
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO,
            @RequestParam String username) {
        try {
            // 1. Obtener el usuario autenticado del servicio
            UserDTO currentUser = inventoryService.getCurrentUser(username); // Usa el método existente

            // 2. Validar permisos
            inventoryService.validatePermission(currentUser, "Reservar_maquinaria");

            // 3. Verificar disponibilidad
            if (!inventoryService.isMachineryAvailable(reservationDTO.getMachinery().getId())) {
                throw new Exception("La maquinaria no está disponible");
            }

            // 4. Asignar usuario a la reserva
            reservationDTO.setUser(currentUser);

            // 5. Crear reserva
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
