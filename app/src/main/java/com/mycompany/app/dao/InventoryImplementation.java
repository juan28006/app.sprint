package com.mycompany.app.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.repositories.InventoryRepository;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.model.Inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
public class InventoryImplementation implements InventoryDao {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception {
        if (inventoryDTO.getUser() == null) {
            throw new Exception("El usuario asociado al inventario no puede ser nulo");
        }

        Inventory inventory = Helpers.parse(inventoryDTO);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return Helpers.parse(savedInventory);
    }

    @Override
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) throws Exception {
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);
        if (optionalInventory.isPresent()) {
            Inventory inventory = optionalInventory.get();

            // Conditional updates based on DTO fields
            if (inventoryDTO.getEntryDate() != null) {
                inventory.setEntryDate(inventoryDTO.getEntryDate());
            }
            if (inventoryDTO.getName() != null) {
                inventory.setName(inventoryDTO.getName());
            }
            if (inventoryDTO.getStatus() != null) {
                inventory.setStatus(inventoryDTO.getStatus());
            }
            if (inventoryDTO.getUser() != null) {
                inventory.setUser(Helpers.parse(inventoryDTO.getUser()));
            }
            if (inventoryDTO.getQuantity() != null) {
                inventory.setQuantity(inventoryDTO.getQuantity());
            }

            Inventory updatedInventory = inventoryRepository.save(inventory);
            return Helpers.parse(updatedInventory);
        }
        return null; // Or throw an exception if you prefer
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id); // Elimina el inventario por su ID
    }

    @Override
    public InventoryDTO getInventoryById(Long id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        return inventory.map(Helpers::parse).orElse(null); // Usa Helpers para convertir
    }
}