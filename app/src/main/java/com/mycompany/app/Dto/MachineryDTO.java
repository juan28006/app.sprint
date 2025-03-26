package com.mycompany.app.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.mycompany.app.Dto.InventoryDTO;

@Getter
@Setter
@NoArgsConstructor
public class MachineryDTO {
    private Long id;
    private String name;
    private String status; // "Operativa", "En Mantenimiento", "Dañada"
    private InventoryDTO inventory;

}