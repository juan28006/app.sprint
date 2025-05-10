package com.mycompany.app.service.Interface;

import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesService {
    ReportInvoicesDTO generateReportInvoices(OrderDTO orderDTO) throws Exception;

    ReportInvoicesDTO updateReportEstado(Long id, String nuevoEstado) throws Exception;

    ReportInvoicesDTO getReportById(Long id) throws Exception; // Renamed from getReportByIdf
}