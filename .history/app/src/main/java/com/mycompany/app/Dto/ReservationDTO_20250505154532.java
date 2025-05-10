/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    private Date reservationDate;
    private String status; // "Pendiente", "Confirmada", "Cancelada"
    private UserDTO user;
    private MachineryDTO machinery;

}