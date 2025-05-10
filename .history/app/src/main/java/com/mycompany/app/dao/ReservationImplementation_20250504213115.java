package com.mycompany.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.ReservationDao;
import com.mycompany.app.dao.repositories.ReservationRepository;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.model.Reservation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
public class ReservationImplementation implements ReservationDao {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<ReservationDTO> getAllReservations() throws Exception {
        try {
            return reservationRepository.findAll().stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener todas las reservaciones: " + e.getMessage());
        }
    }

    @Override
    public ReservationDTO getReservationById(Long id) throws Exception {
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (!reservation.isPresent()) {
                throw new Exception("No se encontró reservación con ID: " + id);
            }
            return Helpers.parse(reservation.get());
        } catch (Exception e) {
            throw new Exception("Error al obtener reservación por ID: " + e.getMessage());
        }
    }

    @Override
    public List<ReservationDTO> getReservationsByStatus(String status) throws Exception {
        try {
            if (status == null || status.trim().isEmpty()) {
                throw new Exception("El estado no puede estar vacío");
            }
            return reservationRepository.findByStatus(status).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reservaciones por estado: " + e.getMessage());
        }
    }

    @Override
    public List<ReservationDTO> getReservationsByUser(Long userId) throws Exception {
        try {
            if (userId == null || userId <= 0) {
                throw new Exception("ID de usuario inválido");
            }
            return reservationRepository.findByUserId(userId).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reservaciones por usuario: " + e.getMessage());
        }
    }

    @Override
    public List<ReservationDTO> getReservationsByMachinery(Long machineryId) throws Exception {
        try {
            if (machineryId == null || machineryId <= 0) {
                throw new Exception("ID de maquinaria inválido");
            }
            return reservationRepository.findByMachineryId(machineryId).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reservaciones por maquinaria: " + e.getMessage());
        }
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception {
        Reservation reservation = Helpers.parse(reservationDTO);
        Reservation savedReservation = reservationRepository.save(reservation);
        return Helpers.parse(savedReservation);
    }

    }

    @Override
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception {
        try {
            if (id == null || id <= 0) {
                throw new Exception("ID de reservación inválido");
            }
            if (reservationDTO == null) {
                throw new Exception("DTO de reservación no puede ser nulo");
            }

            Reservation existing = reservationRepository.findById(id)
                    .orElseThrow(() -> new Exception("Reservación no encontrada con ID: " + id));

            if (!existing.getReservationDate().equals(reservationDTO.getReservationDate())) {
                boolean existsConflict = reservationRepository.existsByMachineryIdAndReservationDate(
                        reservationDTO.getMachinery().getId(),
                        reservationDTO.getReservationDate());

                if (existsConflict) {
                    throw new Exception("Conflicto con otra reservación existente");
                }
            }

            existing.setReservationDate(reservationDTO.getReservationDate());
            existing.setStatus(reservationDTO.getStatus());
            existing.setUser(Helpers.parse(reservationDTO.getUser()));
            existing.setMachinery(Helpers.parse(reservationDTO.getMachinery()));

            Reservation updated = reservationRepository.save(existing);
            return Helpers.parse(updated);
        } catch (Exception e) {
            throw new Exception("Error al actualizar reservación: " + e.getMessage());
        }
    }

    @Override
    public void cancelReservation(Long id) throws Exception {
        try {
            if (id == null || id <= 0) {
                throw new Exception("ID de reservación inválido");
            }

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new Exception("Reservación no encontrada con ID: " + id));

            if ("CANCELADA".equals(reservation.getStatus())) {
                throw new Exception("La reservación ya está cancelada");
            }

            reservation.setStatus("CANCELADA");
            reservationRepository.save(reservation);
        } catch (Exception e) {
            throw new Exception("Error al cancelar reservación: " + e.getMessage());
        }
    }

    @Override
    public List<ReservationDTO> getReservationsByDateRange(Date startDate, Date endDate) throws Exception {
        try {
            if (startDate == null || endDate == null) {
                throw new Exception("Ambas fechas son requeridas");
            }
            if (startDate.after(endDate)) {
                throw new Exception("La fecha de inicio debe ser anterior a la fecha final");
            }

            return reservationRepository.findByReservationDateBetween(startDate, endDate).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reservaciones por rango de fechas: " + e.getMessage());
        }
    }

    @Override
    public void deleteReservation(Long id) throws Exception {
        try {
            if (id == null || id <= 0) {
                throw new Exception("ID de reservación inválido");
            }
            if (!reservationRepository.existsById(id)) {
                throw new Exception("Reservación no encontrada con ID: " + id);
            }
            reservationRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar reservación: " + e.getMessage());
        }
    }
