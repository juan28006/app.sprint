package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.ReservationDTO;
import java.util.Date;
import java.util.List;

public interface ReservationDao {
    // Métodos básicos CRUD
    List<ReservationDTO> getAllReservations() throws Exception;

    ReservationDTO getReservationById(Long id) throws Exception;

    ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception;

    void deleteReservation(Long id) throws Exception;

    // Métodos adicionales de consulta
    List<ReservationDTO> getReservationsByStatus(String status) throws Exception;

    List<ReservationDTO> getReservationsByUser(Long userId) throws Exception;

    List<ReservationDTO> getReservationsByMachinery(Long machineryId) throws Exception;

    List<ReservationDTO> getReservationsByDateRange(Date startDate, Date endDate) throws Exception;

    // Métodos específicos del dominio
    void cancelReservation(Long id) throws Exception;
}