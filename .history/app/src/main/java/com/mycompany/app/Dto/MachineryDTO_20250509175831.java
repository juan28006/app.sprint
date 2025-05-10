package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MachineryDTO {
    private Long id;
    private String name;
    private String status; // "Operativa", "En Mantenimiento", "Dañada"
    private InventoryDTO inventory;

}