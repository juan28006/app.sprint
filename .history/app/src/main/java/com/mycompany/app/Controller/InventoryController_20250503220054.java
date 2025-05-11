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
            if (inventory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El inventario se encuentra vacío");
            }
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al consultar el inventario: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable Long id) {
        try {
            InventoryDTO inventoryDTO = inventoryS.getInventoryById(id);
            if (inventoryDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Registro de inventario no encontrado con ID: " + id);
            }
            return ResponseEntity.ok(inventoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar en inventario: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            if (inventoryDTO.getUser() == null || inventoryDTO.getUser().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Debe especificar un usuario válido para el inventario");
            }

            InventoryDTO createdInventory = inventoryS.createInventory(inventoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Registro de inventario creado exitosamente con ID: " + createdInventory.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear registro de inventario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO updatedInventory = inventoryS.updateInventory(id, inventoryDTO);
            if (updatedInventory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró registro de inventario con ID: " + id);
            }
            return ResponseEntity.ok("Inventario actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar inventario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        try {
            inventoryS.deleteInventory(id);
            return ResponseEntity.ok("Registro de inventario con ID " + id + " eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar registro de inventario: " + e.getMessage());
        }
    }
}