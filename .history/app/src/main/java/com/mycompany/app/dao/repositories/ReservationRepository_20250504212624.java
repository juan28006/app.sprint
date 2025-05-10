package com.mycompany.app.dao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationDTO getReservationById(Long id) throws Exception;

    List<ReservationDTO> getAllReservations() throws Exception;

    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception;

    void deleteReservation(Long id) throws Exception;
}