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
    private String name; // "Generar Informes", "Revisar Maquinaria", etc.

}