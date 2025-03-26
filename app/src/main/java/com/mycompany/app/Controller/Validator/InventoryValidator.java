/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.Dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class InventoryValidator extends CommonsValidator {

    public void validateInventory(InventoryDTO inventoryDTO) {
        if (inventoryDTO.getName() == null || inventoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del inventario no puede estar vacío");
        }
        if (inventoryDTO.getStatus() == null || inventoryDTO.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del inventario no puede estar vacío");
        }
        if (inventoryDTO.getEntryDate() == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede estar vacía");
        }
        if (inventoryDTO.getUser() == null || inventoryDTO.getUser().getId() == null) {
            throw new IllegalArgumentException("El usuario asociado al inventario no puede estar vacío");
        }
    }
}
