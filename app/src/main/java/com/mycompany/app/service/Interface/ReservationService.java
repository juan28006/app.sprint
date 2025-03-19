/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.ReservationDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface ReservationService {
    ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception;

    ReservationDTO getReservationById(Long id) throws Exception;

    List<ReservationDTO> getAllReservations() throws Exception;

    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception;

    void deleteReservation(Long id) throws Exception;
}
