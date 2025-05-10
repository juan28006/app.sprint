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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Request.ClientRegistrationRequest;
import com.mycompany.app.Controller.Validator.ClientValidator;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

@RestController
@RequestMapping("/api/client")
public class ClientController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

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

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deleteClientPerson(@PathVariable Long id) {
        try {
            // Verificar si es cliente antes de eliminar
            UserDTO user = inventoryService.getUserById(id);
            if (!"Cliente".equalsIgnoreCase(user.getTypeUser().getType())) {
                throw new Exception("Solo se pueden eliminar personas de tipo Cliente");
            }

            PersonDTO personToDelete = inventoryService.getPersonById(id);
            inventoryService.deletePerson(personToDelete);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cliente eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Error al eliminar cliente",
                    "message", e.getMessage()));
        }
    }

    @Override
    public void session() throws Exception {
        // Implementación para verificar sesión
    }
}
