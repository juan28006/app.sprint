package com.mycompany.app.Controller;

import java.util.List;
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
                        .body(Map.of(
                                "status", "error",
                                "message", "No se encontró maquinaria registrada",
                                "data", null));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Lista de maquinaria obtenida exitosamente",
                    "count", machinery.size(),
                    "data", machinery));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al obtener maquinaria: " + e.getMessage(),
                            "errorDetails", e.toString()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMachineryById(@PathVariable Long id) {
        try {
            MachineryDTO machineryDTO = machineryService.getMachineryById(id);
            if (machineryDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "Maquinaria no encontrada con ID: " + id,
                                "machineryId", id));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Maquinaria obtenida exitosamente",
                    "data", machineryDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al obtener maquinaria: " + e.getMessage(),
                            "machineryId", id));
        }
    }

    @PostMapping
    public ResponseEntity<?> createMachinery(@RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO createdMachinery = machineryService.createMachinery(machineryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "message", "Maquinaria creada exitosamente",
                    "data", Map.of(
                            "id", createdMachinery.getId(),
                            "name", createdMachinery.getName(),
                            "status", createdMachinery.getStatus(),
                            "inventoryId",
                            createdMachinery.getInventory() != null ? createdMachinery.getInventory().getId() : null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al crear maquinaria: " + e.getMessage(),
                            "inputData", machineryDTO));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMachinery(@PathVariable Long id,
            @RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO updatedMachinery = machineryService.updateMachinery(id, machineryDTO);
            if (updatedMachinery == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "Maquinaria no encontrada con ID: " + id,
                                "machineryId", id));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Maquinaria actualizada exitosamente",
                    "data", updatedMachinery,
                    "changes", Map.of(
                            "name", machineryDTO.getName(),
                            "status", machineryDTO.getStatus())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al actualizar maquinaria: " + e.getMessage(),
                            "machineryId", id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachinery(@PathVariable Long id) {
        try {
            machineryService.deleteMachinery(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Maquinaria eliminada exitosamente",
                    "deletedMachineryId", id,
                    "timestamp", System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al eliminar maquinaria: " + e.getMessage(),
                            "machineryId", id));
        }
    }
}