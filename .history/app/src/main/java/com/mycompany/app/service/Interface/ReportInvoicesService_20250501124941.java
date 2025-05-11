package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;

import java.sql.Date;

public interface ReportInvoicesService {
    ReportInvoicesDTO generarReporteFacturacion(OrderDTO orderDTO) throws Exception;

    List<ReportInvoicesDTO> getReportsByOrder(Long orderId) throws Exception;

    List<ReportInvoicesDTO> getReportsByEstado(String estado) throws Exception;

    List<ReportInvoicesDTO> getReportsByFechaGeneracion(Date inicio, Date fin) throws Exception;

    ReportInvoicesDTO updateReportEstado(Long id, String nuevoEstado) throws Exception;

    ReportInvoicesDTO getReportByIdf(Long id) throws Exception;

    List<ReportInvoicesDTO> getReportsByUsuario(Long userId) throws Exception;
}