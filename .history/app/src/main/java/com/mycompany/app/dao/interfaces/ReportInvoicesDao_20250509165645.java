package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesDao {

    ReportInvoicesDTO createReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;

    List<ReportInvoicesDTO> getReportsByEstado(String estado) throws Exception;

    ReportInvoicesDTO getReportById(Long id) throws Exception; // Renamed from getReportFacturacionById

    void deleteReportInvoices(Long id) throws Exception; // Nuevo método añadido

    List<ReportInvoicesDTO> getReportsByOrderId(Long orderId) throws Exception;

    ReportInvoicesDTO updateReportFacturacion(ReportInvoicesDTO existingReportDTO) throws Exception;

}
