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
// PUT /api/orders/{id}/status → Actualizar estado
@Getter
@Setter
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    private Long id;
    private String status; // "Pendiente", "Enviado", "Completado"

}
