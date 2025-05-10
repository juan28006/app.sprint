/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.InventoryDTO;

import org.springframework.stereotype.Component;

@Component
public class MachineryValidator {

    public void validateMachinery(MachineryDTO machineryDTO) throws Exception {
        if (machineryDTO.getName() == null || machineryDTO.getName().isEmpty()) {
            throw new Exception("El nombre de la maquinaria no puede estar vacío");
        }
        if (machineryDTO.getStatus() == null || machineryDTO.getStatus().isEmpty()) {
            throw new Exception("El estado de la maquinaria no puede estar vacío");
        }
        if (machineryDTO.getInventory() == null || machineryDTO.getInventory().getId() == null) {
            throw new Exception("El inventario asociado a la maquinaria no puede estar vacío");
        }
    }
}