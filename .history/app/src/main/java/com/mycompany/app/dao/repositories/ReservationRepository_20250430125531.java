package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Métodos básicos
    List<Reservation> findByStatus(String status);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByMachineryId(Long machineryId);

    // Métodos para manejo de fechas
    boolean existsByMachineryIdAndReservationDate(Long machineryId, Date reservationDate);

    List<Reservation> findByReservationDateBetween(Date startDate, Date endDate);

    List<Reservation> findByUserIdAndReservationDate(Long userId, Date reservationDate);

    // Métodos combinados
    List<Reservation> findByUserIdAndStatus(Long userId, String status);

    List<Reservation> findByMachineryIdAndStatus(Long machineryId, String status);
}