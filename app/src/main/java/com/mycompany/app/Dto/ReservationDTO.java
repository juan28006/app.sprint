/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

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