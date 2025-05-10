package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesService {

    List<ReportInvoicesDTO> getReportsByStatus(String estado) throws Exception;

    ReportInvoicesDTO getReportById(Long id) throws Exception; // Renamed from getReportByIdf

    void deleteReportInvoices(Long id) throws Exception; // Nuevo método añadido

    ReportInvoicesDTO generateReportInvoices(ReportInvoicesDTO report) throws Exception;

    ReportInvoicesDTO updateReportFacturacion(Long id, ReportInvoicesDTO reportDTO) throws Exception;

}