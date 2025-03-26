package com.mycompany.app.Controller;

import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.service.Interface.InventoryS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryS inventoryS;

    @GetMapping
    public List<InventoryDTO> getAllInventory() throws Exception {
        return inventoryS.getAllInventory();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) throws Exception {
        InventoryDTO inventoryDTO = inventoryS.getInventoryById(id);
        return ResponseEntity.ok(inventoryDTO);
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@RequestBody InventoryDTO inventoryDTO) throws Exception {
        InventoryDTO createdInventory = inventoryS.createInventory(inventoryDTO);
        return ResponseEntity.ok(createdInventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long id,
            @RequestBody InventoryDTO inventoryDTO) throws Exception {
        InventoryDTO updatedInventory = inventoryS.updateInventory(id, inventoryDTO);
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) throws Exception {
        inventoryS.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}