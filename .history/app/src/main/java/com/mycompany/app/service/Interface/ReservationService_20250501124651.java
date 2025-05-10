package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.ReservationDTO;

public interface ReservationService {

    List<ReservationDTO> getAllReservations() throws Exception;

    ReservationDTO getReservationById(Long id) throws Exception;

    ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception;

    void deleteReservation(Long id) throws Exception;

}