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

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.service.Interface.InventoryS;

@RestController
@RequestMapping("/api/inventory")

public class InventoryController {

    @Autowired
    private InventoryS inventoryS;

    @GetMapping
    public ResponseEntity<?> getAllInventory() {
        try {
            List<InventoryDTO> inventory = inventoryS.getAllInventory();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Inventario obtenido exitosamente",
                    "data", inventory,
                    "count", inventory.size()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error al obtener inventario",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable Long id) {
        try {
            InventoryDTO inventoryDTO = inventoryS.getInventoryById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Registro de inventario obtenido exitosamente",
                    "data", inventoryDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Registro de inventario no encontrado",
                    "message", e.getMessage(),
                    "inventoryId", id));
        }
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            if (inventoryDTO.getUser() == null || inventoryDTO.getUser().getId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Validación fallida",
                        "message", "El usuario asociado al inventario es requerido"));
            }

            InventoryDTO createdInventory = inventoryS.createInventory(inventoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Inventario creado exitosamente",
                    "inventoryId", createdInventory.getId(),
                    "status", createdInventory.getStatus(),
                    "userId", createdInventory.getUser().getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al crear inventario",
                    "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id,
            @RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO updatedInventory = inventoryS.updateInventory(id, inventoryDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Inventario actualizado exitosamente",
                    "inventoryId", updatedInventory.getId(),
                    "newStatus", updatedInventory.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al actualizar inventario",
                    "message", e.getMessage(),
                    "inventoryId", id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        try {
            inventoryS.deleteInventory(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Inventario eliminado exitosamente",
                    "inventoryId", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al eliminar inventario",
                    "message", e.getMessage(),
                    "inventoryId", id));
        }
    }
}