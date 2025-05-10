package com.mycompany.app.Controller.Validator;

import org.springframework.stereotype.Component;

import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;

@Component
public class ReportInvoicesValidator {
    public void validateUserId(Long userId) throws Exception {
        if (userId == null || userId <= 0) {
            throw new Exception("El ID de usuario debe ser válido");
        }
    }

    public void validateOrderForInvoice(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            throw new Exception("La orden no puede ser nula");
        }
        if (orderDTO.getId() == null) {
            throw new Exception("La orden debe tener un ID válido");
        }
        if (orderDTO.getStatus() == null || !orderDTO.getStatus().equals("Aprobada")) {
            throw new Exception("Solo se pueden generar facturas para órdenes aprobadas");
        }
    }

    public void validateReportInvoice(ReportInvoicesDTO reportDTO) throws Exception {
        if (reportDTO == null) {
            throw new Exception("El reporte de factura no puede ser nulo");
        }
        if (reportDTO.getCodigoFactura() == null || reportDTO.getCodigoFactura().isEmpty()) {
            throw new Exception("El código de factura es requerido");
        }
        if (reportDTO.getUsuario() == null || reportDTO.getUsuario().getId() == null) {
            throw new Exception("El usuario asociado es requerido");
        }
    }

    public static void validateReportInvoiceDTO(ReportInvoicesDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("El DTO de reporte de factura no puede ser nulo.");
        }
        if (dto.getId() == null || dto.getId() <= 0) {
            throw new Exception("ID de factura inválido.");
        }
        // Add other validation rules as needed (e.g., check for valid date formats,
        // etc.)
    }

    public void validateInvoiceId(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de factura inválido");
        }
    }

}
