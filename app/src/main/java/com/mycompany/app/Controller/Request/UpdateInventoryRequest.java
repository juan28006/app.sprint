/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import com.mycompany.app.dto.InventoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
// PUT /api/inventory/{id} → Actualizar ítem
@Getter
@Setter
@NoArgsConstructor
public class UpdateInventoryRequest {
    private Long id;
    private InventoryDTO inventoryDTO; // "Disponible", "Agotado""

}
