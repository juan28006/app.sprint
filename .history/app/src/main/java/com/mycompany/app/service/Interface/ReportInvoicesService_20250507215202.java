package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;

public interface ReportInvoicesService {

    ReportInvoicesDTO generateReportInvoices(OrderDTO orderDTO) throws Exception;

    List<ReportInvoicesDTO> getReportsByStatus(String estado) throws Exception;

    ReportInvoicesDTO getReportById(Long id) throws Exception; // Renamed from getReportByIdf

    void deleteReportInvoices(Long id) throws Exception; // Nuevo método añadido

}