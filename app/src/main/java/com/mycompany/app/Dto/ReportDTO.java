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

public class ReportDTO {
    private Long id;
    private String type; // "Uso", "Estado", "Mantenimiento"
    private Date generationDate;
    private ReservationDTO user;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getGenerationDate() {
        return this.generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public ReservationDTO getUser() {
        return this.user;
    }

    public void setUser(ReservationDTO user) {
        this.user = user;
    }

    // Getters and Setters
}