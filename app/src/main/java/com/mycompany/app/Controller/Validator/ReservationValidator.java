package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.ReservationDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {

    public void validateReservation(ReservationDTO reservationDTO) throws Exception {
        if (reservationDTO.getReservationDate() == null) {
            throw new Exception("La fecha de reserva no puede estar vacía");
        }
        if (reservationDTO.getStatus() == null || reservationDTO.getStatus().isEmpty()) {
            throw new Exception("El estado de la reserva no puede estar vacío");
        }
        if (reservationDTO.getUser() == null || reservationDTO.getUser().getId() == null) {
            throw new Exception("El usuario asociado a la reserva no puede estar vacío");
        }
        if (reservationDTO.getMachinery() == null || reservationDTO.getMachinery().getId() == null) {
            throw new Exception("La maquinaria asociada a la reserva no puede estar vacía");
        }
    }
}