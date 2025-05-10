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
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Request.EmployeeRegistrationRequest;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dto.AccessRequestDTO;
import com.mycompany.app.dto.EmployeeDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

@RestController
@RequestMapping("/api/empleado")
public class EmployeeController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRegistrationRequest request) { // Cambio a // //
                                                                                                  // @RequestBody
        try {
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
            UserDTO createdUser = inventoryService.createEmpleado(userDTO);

            // 3. Opcional: Crear EmployeeDTO si necesitas más datos
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setUser(Helpers.parse(createdUser));
            employeeDTO.setDepartment(request.getDepartment());

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

    // Session Management
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
