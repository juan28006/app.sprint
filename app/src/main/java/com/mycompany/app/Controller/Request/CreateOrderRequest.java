/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import com.mycompany.app.dto.OrderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
// POST /api/orders → Crear orden
@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequest {
    private OrderDTO orderDTO;
}
