package com.mycompany.app.Dto;

import java.util.Date;

public class InventoryDTO {
    private Long id; // ID del inventario
    private String name; // Nombre del inventario (si es necesario)
    private String status; // Estado del inventario: "Operativa", "En Mantenimiento", "Dañada"
    private Date entryDate; // Fecha de ingreso del inventario
    private ReservationDTO user; // Objeto UserDTO en lugar de un Long userId

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public ReservationDTO getUser() {
        return user;
    }

    public void setUser(ReservationDTO user) {
        this.user = user;
    }
}