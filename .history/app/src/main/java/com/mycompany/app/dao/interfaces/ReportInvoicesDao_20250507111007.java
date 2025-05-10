package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesDao {

    ReportInvoicesDTO createReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;

    ReportInvoicesDTO getReportFacturacionById(Long id) throws Exception;

    List<ReportInvoicesDTO> getReportsByOrderId(Long orderId) throws Exception;

    ReportInvoicesDTO getReportById(Long id) throws Exception; // Renamed from getReportFacturacionById

    ReportInvoicesDTO updateReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;

    List<ReportInvoicesDTO> getReportsByUsuarioId(Long userId) throws Exception;
}
