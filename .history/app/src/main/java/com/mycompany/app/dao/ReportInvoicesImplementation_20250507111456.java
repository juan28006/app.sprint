package com.mycompany.app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.ReportInvoicesDao;
import com.mycompany.app.dao.repositories.ReportInvoicesRepository;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.model.ReportInvoices;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public ReportInvoicesDTO getReportById(Long id) throws Exception {
        try {
            return reportInvoicesRepository.findById(id)
                    .map(Helpers::parse)
                    .orElseThrow(() -> new Exception("Factura no encontrada con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al obtener factura: " + e.getMessage());
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
}