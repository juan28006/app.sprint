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
public class PermissionDTO {
    private Long id;
    private String name; // "Generar Informes", "Revisar Maquinaria", etc.

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

}