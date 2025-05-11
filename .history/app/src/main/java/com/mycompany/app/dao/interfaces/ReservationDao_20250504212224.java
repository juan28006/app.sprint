package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.ReservationDTO;

public interface ReservationDao {
    ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationDTO getReservationById(Long id) throws Exception;

    List<ReservationDTO> getAllReservations() throws Exception;

    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception;

    void deleteReservation(Long id) throws Exception;
}