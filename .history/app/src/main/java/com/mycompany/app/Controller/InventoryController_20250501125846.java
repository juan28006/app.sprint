package com.mycompany.app.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        try {
            List<InventoryDTO> inventory = inventoryS.getAllInventory();
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable Long id) {
        try {
            InventoryDTO inventoryDTO = inventoryS.getInventoryById(id);
            return ResponseEntity.ok(inventoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO createdInventory = inventoryS.createInventory(inventoryDTO);
            return ResponseEntity.ok(createdInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id,
            @RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO updatedInventory = inventoryS.updateInventory(id, inventoryDTO);
            return ResponseEntity.ok(updatedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        try {
            inventoryS.deleteInventory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}