package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.ReservationDTO;
import java.util.List;

public interface ReservationDao {
    List<ReservationDTO> getAllReservations() throws Exception;

    ReservationDTO getReservationById(Long id) throws Exception;

    ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception;

    void deleteReservation(Long id) throws Exception;

}
