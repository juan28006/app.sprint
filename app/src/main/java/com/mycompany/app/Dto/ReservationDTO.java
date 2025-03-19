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
import java.util.Date;

public class ReservationDTO {
    private Long id;
    private Date reservationDate;
    private String status; // "Pendiente", "Confirmada", "Cancelada"
    private UserDTO user;
    private MachineryDTO machinery;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReservationDate() {
        return this.reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return this.user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public MachineryDTO getMachinery() {
        return this.machinery;
    }

    public void setMachinery(MachineryDTO machinery) {
        this.machinery = machinery;
    }

}