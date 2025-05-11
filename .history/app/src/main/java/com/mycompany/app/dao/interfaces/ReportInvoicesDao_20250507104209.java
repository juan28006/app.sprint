package com.mycompany.app.dao.interfaces;

import java.sql.Date;
import java.util.List;

import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesDao {

    ReportInvoicesDTO createReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;

    ReportInvoicesDTO getReportFacturacionById(Long id) throws Exception;

    List<ReportInvoicesDTO> getReportsByOrderId(Long orderId) throws Exception;

    List<ReportInvoicesDTO> getReportsByEstado(String estado) throws Exception;

    List<ReportInvoicesDTO> getReportsByFechaGeneracionBetween(Date inicio, Date fin) throws Exception;

    ReportInvoicesDTO getInvoiceById(Long id) throws Exception; // Renamed from getReportFacturacionById

    ReportInvoicesDTO updateReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;

    List<ReportInvoicesDTO> getReportsByUsuarioId(Long userId) throws Exception;
}
