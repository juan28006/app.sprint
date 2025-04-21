/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
// POST /api/machinery → Añadir maquinaria
@Getter
@Setter
@NoArgsConstructor
public class CreateMachineryRequest {
    private String name;
    private String model;
    private String status;

}
