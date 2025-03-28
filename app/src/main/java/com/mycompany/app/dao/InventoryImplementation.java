package com.mycompany.app.dao;

import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.repositories.InventoryRepository;
import com.mycompany.app.model.Inventory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.mycompany.app.Helpers.Helpers;

@Service
@NoArgsConstructor
@Setter
@Getter
public class InventoryImplementation implements InventoryDao {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = Helpers.parse(inventoryDTO); // Convierte InventoryDTO a Inventory
        Inventory savedInventory = inventoryRepository.save(inventory); // Guarda en la base de datos
        return Helpers.parse(savedInventory); // Convierte el resultado a InventoryDTO
    }

    @Override
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);
        if (optionalInventory.isPresent()) {
            Inventory inventory = optionalInventory.get();
            inventory.setEntryDate(inventoryDTO.getEntryDate()); // Actualiza la fecha de ingreso
            inventory.setStatus(inventoryDTO.getStatus()); // Actualiza el estado
            inventory.setUser(Helpers.parse(inventoryDTO.getUser())); // Actualiza el usuario asociado
            Inventory updatedInventory = inventoryRepository.save(inventory); // Guarda los cambios
            return Helpers.parse(updatedInventory); // Convierte el resultado a InventoryDTO
        }
        return null; // Si no se encuentra el inventario, retorna null
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id); // Elimina el inventario por su ID
    }

    @Override
    public List<InventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(Helpers::parse) // Usa Helpers para convertir Inventory a InventoryDTO
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDTO getInventoryById(Long id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        return inventory.map(Helpers::parse).orElse(null); // Usa Helpers para convertir
    }

    @Override
    public InventoryDTO getInventoryByMachineryId(Long machineryId) throws Exception {
        try {
            List<Inventory> inventoryList = inventoryRepository.findByMachineryId(machineryId);

            if (inventoryList.isEmpty()) {
                throw new Exception("No se encontró inventario para la maquinaria con ID: " + machineryId);
            }

            // Asumimos que solo hay un registro de inventario por maquinaria
            Inventory inventory = inventoryList.get(0);
            return Helpers.parse(inventory);

        } catch (Exception e) {
            throw new Exception("Error al obtener inventario por MachineryId: " + e.getMessage());
        }
    }
}