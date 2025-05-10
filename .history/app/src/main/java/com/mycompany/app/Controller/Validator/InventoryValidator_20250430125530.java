package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.InventoryDTO;
import org.springframework.stereotype.Component;

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
        if (inventoryDTO.getUser() == null || inventoryDTO.getUser().getId() == null) {
            throw new Exception("El usuario asociado al inventario no puede estar vacío");
        }
    }
}
