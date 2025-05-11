package com.mycompany.app.Controller;

import java.util.List;

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

import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.service.Interface.MachineryService;

@RestController
@RequestMapping("/api/machinery")

public class MachineryController {

    @Autowired
    private MachineryService machineryService;

    @GetMapping
    public ResponseEntity<?> getAllMachinery() {
        try {
            List<MachineryDTO> machinery = machineryService.getAllMachinery();
            if (machinery == null || machinery.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró maquinaria registrada");
            }
            return ResponseEntity.ok(machinery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener maquinaria: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMachineryById(@PathVariable Long id) {
        try {
            MachineryDTO machineryDTO = machineryService.getMachineryById(id);
            if (machineryDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Maquinaria no encontrada con ID: " + id);
            }
            return ResponseEntity.ok(machineryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener maquinaria: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createMachinery(@RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO createdMachinery = machineryService.createMachinery(machineryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMachinery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear maquinaria: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMachinery(@PathVariable Long id,
            @RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO updatedMachinery = machineryService.updateMachinery(id, machineryDTO);
            if (updatedMachinery == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Maquinaria no encontrada con ID: " + id);
            }
            return ResponseEntity.ok(updatedMachinery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar maquinaria: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachinery(@PathVariable Long id) {
        try {
            machineryService.deleteMachinery(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar maquinaria: " + e.getMessage());
        }
    }
}