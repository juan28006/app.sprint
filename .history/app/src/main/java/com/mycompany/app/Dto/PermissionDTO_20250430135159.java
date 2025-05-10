package com.mycompany.app.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Farley
 */
@Getter
@Setter
@NoArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name;

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
    // "Generar Informes", "Revisar Maquinaria", etc.

}