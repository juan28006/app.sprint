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

import com.mycompany.app.Controller.Request.EmployeeRegistrationRequest;
import com.mycompany.app.Controller.Validator.EmployeeValidator;
import com.mycompany.app.dto.AccessRequestDTO;
import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

@RestController
@RequestMapping("/api/empleado")
public class EmployeeController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private EmployeeValidator employeeValidator;

    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRegistrationRequest request) {// @RequestBody
        try {
            employeeValidator.validateForCreation(request);

            // 1. Registrar Persona
            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(request.getName());
            personDTO.setDocument(request.getDocument());
            personDTO.setCellphone(request.getCellphone());
            inventoryService.createPerson(personDTO);

            // 2. Registrar Usuario (Empleado)
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(request.getUsername());
            userDTO.setPassword(request.getPassword());
            // Establecer la relación con la persona recién creada
            userDTO.setPerson(personDTO); // ← Aquí está el cambi
            UserDTO createdUser = inventoryService.createEmpleado(userDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", createdUser.getId(),
                    "message", "Empleado registrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al registrar empleado",
                    "message", e.getMessage()));
        }
    }

    @PostMapping("/access")
    public ResponseEntity<?> controlAccess(@RequestBody AccessRequestDTO accessDTO) {
        try {
            String accessResult = inventoryService.controlAccess(accessDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", accessResult,
                    "message", "Acceso controlado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "Error al controlar acceso",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Machinery View
    @GetMapping("/machinery")
    public ResponseEntity<?> viewMachinery() {
        try {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", inventoryService.getAllMachinery(),
                    "message", "Maquinaria obtenida exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al obtener maquinaria",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deleteEmployeePerson(@PathVariable Long id) {
        try {
            // Verificar si es empleado antes de eliminar
            UserDTO user = inventoryService.getUserById(id);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Usuario no encontrado",
                                "message", "No existe un usuario con ID: " + id));
            }
            if (!"Empleado".equalsIgnoreCase(user.getTypeUser().getType())) {
                throw new Exception("Solo se pueden eliminar personas de tipo Empleado");
            }
            // 2. Eliminar el usuario
            inventoryService.deleteUser(id);

            if (user.getPerson() != null) {
                inventoryService.deletePerson(user.getPerson());
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Empleado eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Error al eliminar empleado",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/machinery/{id}")
    public ResponseEntity<?> getMachineryById(@PathVariable Long id) {
        try {
            MachineryDTO machineryDTO = inventoryService.getMachineryById(id);
            if (machineryDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Maquinaria no encontrada con ID: " + id);
            }
            return ResponseEntity.ok(machineryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar maquinaria: " + e.getMessage());
        }
    }

    @PostMapping("/machinery")
    public ResponseEntity<?> createMachinery(@RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO createdMachinery = inventoryService.createMachinery(machineryDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Maquinaria '" + createdMachinery.getName() + "' registrada exitosamente con ID: "
                            + createdMachinery.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar maquinaria: " + e.getMessage());
        }
    }

    @PutMapping("/machinery/{id}")
    public ResponseEntity<?> updateMachinery(@PathVariable Long id, @RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO updatedMachinery = inventoryService.updateMachinery(id, machineryDTO);
            if (updatedMachinery == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró maquinaria con ID: " + id);
            }
            return ResponseEntity.ok("Maquinaria '" + updatedMachinery.getName() + "' actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar maquinaria: " + e.getMessage());
        }
    }

    @DeleteMapping("/machinery/{id}")
    public ResponseEntity<?> deleteMachinery(@PathVariable Long id) {
        try {
            inventoryService.deleteMachinery(id);
            return ResponseEntity.ok("Maquinaria con ID " + id + " eliminada del sistema");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar maquinaria: " + e.getMessage());
        }
    }

    @Override
    public void session() throws Exception {

    }
}
