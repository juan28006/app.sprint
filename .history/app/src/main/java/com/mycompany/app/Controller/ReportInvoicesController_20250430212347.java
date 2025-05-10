package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.app.Dto.ReportInvoicesDTO;
import com.mycompany.app.Controller.Validator.ReportInvoicesValidator;
import com.mycompany.app.Dto.OrderDTO;
import com.mycompany.app.service.Interface.ReportInvoicesService;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reports-invoices")
public class ReportInvoicesController {

    @Autowired
    private ReportInvoicesService reportService;

    @Autowired
    private ReportInvoicesValidator validator;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> getFacturasByUsuario(@PathVariable Long userId) {
        try {
            validator.validateUserId(userId);
            List<ReportInvoicesDTO> reports = reportService.getReportsByUsuario(userId);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody OrderDTO orderDTO) {
        try {
            ReportInvoicesDTO report = reportService.generarReporteFacturacion(orderDTO);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orden/{orderId}")
    public ResponseEntity<?> getByOrderId(@PathVariable Long orderId) {
        try {
            List<ReportInvoicesDTO> reports = reportService.getReportsByOrder(orderId);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getByEstado(@PathVariable String estado) {
        try {
            List<ReportInvoicesDTO> reports = reportService.getReportsByEstado(estado);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/fechas")
    public ResponseEntity<?> getByFechas(@RequestParam Date inicio, @RequestParam Date fin) {
        try {
            List<ReportInvoicesDTO> reports = reportService.getReportsByFechaGeneracion(inicio, fin);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long id,
            @RequestParam String nuevoEstado) {
        try {
            ReportInvoicesDTO updatedReport = reportService.updateReportEstado(id, nuevoEstado);
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}