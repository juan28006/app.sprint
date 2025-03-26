/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.ReservationDTO;
import com.mycompany.app.Dto.MachineryDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {

    public void validateReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getReservationDate() == null) {
            throw new IllegalArgumentException("La fecha de reserva no puede estar vacía");
        }
        if (reservationDTO.getStatus() == null || reservationDTO.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado de la reserva no puede estar vacío");
        }
        if (reservationDTO.getUser() == null || reservationDTO.getUser().getId() == null) {
            throw new IllegalArgumentException("El usuario asociado a la reserva no puede estar vacío");
        }
        if (reservationDTO.getMachinery() == null || reservationDTO.getMachinery().getId() == null) {
            throw new IllegalArgumentException("La maquinaria asociada a la reserva no puede estar vacía");
        }
    }
}