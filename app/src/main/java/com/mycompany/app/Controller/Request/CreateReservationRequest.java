/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
// POST /api/reservations → Crear reserva
@Getter
@Setter
@NoArgsConstructor
public class CreateReservationRequest {
    private Long userId;
    private Long machineryId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
