package com.mycompany.app.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.ReportInvoicesDao;
import com.mycompany.app.dao.repositories.OrderRepository;
import com.mycompany.app.dao.repositories.ReportInvoicesRepository;
import com.mycompany.app.dao.repositories.UserRepository;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.model.Order;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ReportInvoicesDTO createReportFacturacion(ReportInvoicesDTO reportDTO) throws Exception {
        try {
            if (reportDTO.getCodigoFactura() == null || reportDTO.getTotal() == null ||
                    reportDTO.getFechaGeneracion() == null || reportDTO.getOrder() == null) {
                throw new Exception("Todos los campos de la factura son obligatorios");
            }
            ReportInvoices report = Helpers.parse(reportDTO);
            ReportInvoices savedReport = reportInvoicesRepository.save(report);
            return Helpers.parse(savedReport);
        } catch (Exception e) {
            throw new Exception("Error al crear reporte de facturación: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByEstado(String estado) throws Exception {
        try {
            return reportInvoicesRepository.findByEstado(estado).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por estado: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByOrderId(Long orderId) throws Exception {
        try {
            return reportInvoicesRepository.findByOrderId(orderId).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener facturas por ID de orden: " + e.getMessage());
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
    public ReportInvoicesDTO updateReportFacturacion(Long id, ReportInvoicesDTO reportDTO) throws Exception {
        try {
            // 1. Verificar que existe la factura
            ReportInvoices existingReport = reportInvoicesRepository.findById(id)
                    .orElseThrow(() -> new Exception("Factura no encontrada con ID: " + id));

            // 2. Actualizar campos básicos (si vienen en el DTO)
            if (reportDTO.getEstado() != null) {
                existingReport.setEstado(reportDTO.getEstado());
            }
            if (reportDTO.getCodigoFactura() != null) {
                existingReport.setCodigoFactura(reportDTO.getCodigoFactura());
            }
            if (reportDTO.getTotal() != null) {
                existingReport.setTotal(reportDTO.getTotal());
            }
            if (reportDTO.getFechaGeneracion() != null) {
                existingReport.setFechaGeneracion(reportDTO.getFechaGeneracion());
            }

            // 3. Manejar relación con Order (solo si viene en el DTO y es diferente)
            if (reportDTO.getOrder() != null && reportDTO.getOrder().getId() != null) {
                // Verificar si la orden existe
                Order order = orderRepository.findById(reportDTO.getOrder().getId())
                        .orElseThrow(() -> new Exception("No se puede actualizar: Orden con ID " +
                                reportDTO.getOrder().getId() + " no existe"));

                // Validar que la orden esté aprobada si se marca como pagada
                if ("Pagada".equalsIgnoreCase(reportDTO.getEstado()) &&
                        !"Aprobada".equalsIgnoreCase(order.getStatus())) {
                    throw new Exception("Solo se pueden pagar facturas de órdenes aprobadas");
                }

                existingReport.setOrder(order);
            }

            // 4. Guardar cambios
            ReportInvoices updatedReport = reportInvoicesRepository.save(existingReport);
            return Helpers.parse(updatedReport);
        } catch (Exception e) {
            throw new Exception("Error al actualizar factura: " + e.getMessage());
        }
    }

    @Override
    public void deleteReportInvoices(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de factura inválido: " + id);
        }
        try {
            if (!reportInvoicesRepository.existsById(id)) {
                throw new Exception("Factura no encontrada con ID: " + id);
            }
            reportInvoicesRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar factura: " + e.getMessage());
        }
    }
}