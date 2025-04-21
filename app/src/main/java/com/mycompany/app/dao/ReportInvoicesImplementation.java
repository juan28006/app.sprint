package com.mycompany.app.dao;

import com.mycompany.app.Dto.ReportInvoicesDTO;
import com.mycompany.app.dao.interfaces.ReportInvoicesDao;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.ReportInvoicesRepository;
import com.mycompany.app.model.ReportInvoices;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.sql.Date;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
@Service
public class ReportInvoicesImplementation implements ReportInvoicesDao {

    @Autowired
    private ReportInvoicesRepository reportInvoicesRepository;

    @Override
    public ReportInvoicesDTO createReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception {
        try {
            ReportInvoices report = Helpers.parse(reportDTO);
            ReportInvoices savedReport = reportInvoicesRepository.save(report);
            return Helpers.parse(savedReport);
        } catch (Exception e) {
            throw new Exception("Error al crear reporte de facturación: " + e.getMessage());
        }
    }

    @Override
    public ReportInvoicesDTO getReportFacturacionById(Long id) throws Exception {
        try {
            return reportInvoicesRepository.findById(id)
                    .map(Helpers::parse)
                    .orElseThrow(() -> new Exception("Reporte no encontrado con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al obtener reporte: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByOrderId(Long orderId) throws Exception {
        try {
            return reportInvoicesRepository.findByOrderId(orderId).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por orden: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByEstado(String estado) throws Exception {
        try {
            return reportInvoicesRepository.findByEstado(estado).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por estado: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByFechaGeneracionBetween(Date inicio, Date fin) throws Exception {
        try {
            return reportInvoicesRepository.findByFechaGeneracionBetween(inicio, fin)
                    .stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por rango de fechas: " + e.getMessage());
        }
    }

    @Override
    public ReportInvoicesDTO updateReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception {
        try {
            ReportInvoices existingReport = reportInvoicesRepository.findById(reportDTO.getId())
                    .orElseThrow(() -> new Exception("Reporte no encontrado con ID: " + reportDTO.getId()));

            existingReport.setEstado(reportDTO.getEstado());
            ReportInvoices updatedReport = reportInvoicesRepository.save(existingReport);
            return Helpers.parse(updatedReport);
        } catch (Exception e) {
            throw new Exception("Error al actualizar reporte: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByUsuarioId(Long userId) throws Exception {
        try {
            return reportInvoicesRepository.findByUsuarioId(userId).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por usuario: " + e.getMessage());
        }
    }
}