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
            if (machinery.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay maquinaria registrada en el sistema");
            }
            return ResponseEntity.ok(machinery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el listado de maquinaria: " + e.getMessage());
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
                    .body("Error al buscar maquinaria: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createMachinery(@RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO createdMachinery = machineryService.createMachinery(machineryDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Maquinaria '" + createdMachinery.getName() + "' registrada exitosamente con ID: "
                            + createdMachinery.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar maquinaria: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMachinery(@PathVariable Long id, @RequestBody MachineryDTO machineryDTO) {
        try {
            MachineryDTO updatedMachinery = machineryService.updateMachinery(id, machineryDTO);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachinery(@PathVariable Long id) {
        try {
            machineryService.deleteMachinery(id);
            return ResponseEntity.ok("Maquinaria con ID " + id + " eliminada del sistema");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar maquinaria: " + e.getMessage());
        }
    }
}