/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Dto;

/**
 *
 * @author Farley
 */
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