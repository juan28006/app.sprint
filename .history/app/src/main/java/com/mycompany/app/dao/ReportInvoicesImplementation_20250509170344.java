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
import com.mycompany.app.model.User;

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
    public ReportInvoicesDTO updateReportFacturacion(ReportInvoicesDTO existingReport) throws Exception {
        try {
            // 2. Verificar que existe la orden
            if (reportDTO.getOrder() == null || reportDTO.getOrder().getId() == null) {
                throw new Exception("Se requiere una orden válida");
            }

            Order order = orderRepository.findById(reportDTO.getOrder().getId())
                    .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + reportDTO.getOrder().getId()));

            // 3. Verificar que existe el usuario
            if (reportDTO.getUsuario() == null || reportDTO.getUsuario().getId() == null) {
                throw new Exception("Se requiere un usuario válido");
            }

            User user = userRepository.findById(reportDTO.getUsuario().getId())
                    .orElseThrow(
                            () -> new Exception("Usuario no encontrado con ID: " + reportDTO.getUsuario().getId()));

            // Actualización de campos
            if (reportDTO.getCodigoFactura() != null) {
                existingReport.setCodigoFactura(reportDTO.getCodigoFactura());
            }
            if (reportDTO.getFechaGeneracion() != null) {
                existingReport.setFechaGeneracion(reportDTO.getFechaGeneracion());
            }
            if (reportDTO.getEstado() != null) {
                existingReport.setEstado(reportDTO.getEstado());
            }
            if (reportDTO.getTotal() != null) {
                existingReport.setTotal(reportDTO.getTotal());
            }

            // Establecer relaciones
            existingReport.setOrder(order);
            existingReport.setUsuario(user);

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