package com.mycompany.app.Controller.Validator;

import org.springframework.stereotype.Component;

import com.mycompany.app.dto.InventoryDTO;

@Component
public class InventoryValidator extends CommonsValidator {

    public void validateInventory(InventoryDTO inventoryDTO) throws Exception {

        if (inventoryDTO.getName() == null || inventoryDTO.getName().isEmpty()) {
            throw new Exception("El nombre del inventario no puede estar vacío");
        }
        if (inventoryDTO.getStatus() == null || inventoryDTO.getStatus().isEmpty()) {
            throw new Exception("El estado del inventario no puede estar vacío");
        }
        if (inventoryDTO.getEntryDate() == null) {
            throw new Exception("La fecha de ingreso no puede estar vacía");
        }
        // Validación mejorada para el usuario
        if (inventoryDTO.getUser() == null) {
            throw new Exception("El usuario asociado al inventario no puede estar vacío");
        }
        if (inventoryDTO.getUser().getId() == null) {
            throw new Exception("El ID del usuario asociado no puede estar vacío");
        }
    }
}
