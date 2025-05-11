package com.mycompany.app.dao.interfaces;

import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesDao {

    ReportInvoicesDTO createReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;

    ReportInvoicesDTO getReportById(Long id) throws Exception; // Renamed from getReportFacturacionById

    ReportInvoicesDTO updateReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception;
}
