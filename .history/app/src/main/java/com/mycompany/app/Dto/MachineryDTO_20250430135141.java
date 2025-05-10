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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InventoryDTO getInventory() {
        return this.inventory;
    }

    public void setInventory(InventoryDTO inventory) {
        this.inventory = inventory;
    }

}