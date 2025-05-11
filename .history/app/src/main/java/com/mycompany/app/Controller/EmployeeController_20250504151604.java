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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/empleado")
public class EmployeeController {
    @Autowired
    private InventoryService inventoryService;

    // Controlar_acceso
    @PostMapping("/acceso")
    public ResponseEntity<?> controlarAcceso(@RequestBody AccessRequestDTO accessDTO) {
        try {
            return ResponseEntity.ok(inventoryService.controlAccess(accessDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar_disponibilidad
    @GetMapping("/maquinaria")
    public ResponseEntity<?> consultarMaquinaria() {
        try {
            return ResponseEntity.ok(inventoryService.getAllMachinery());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
