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
@RequestMapping("/api/reportes-facturacion")
public class ReportInvoicesController {

    @Autowired
    private ReportInvoicesService reportService;

    @Autowired
    private ReportInvoicesValidator validator;

    // Método nuevo para obtener facturas por usuario
    @GetMapping("/{userId}")
    public ResponseEntity<List<ReportInvoicesDTO>> getFacturasByUsuario(Long userId) throws Exception {
        validator.validateUserId(userId);
        return ResponseEntity.ok(reportService.getReportsByUsuario(userId));
    }

    @PostMapping("/generar")
    public ResponseEntity<ReportInvoicesDTO> generarFactura(@RequestBody OrderDTO orderDTO) throws Exception {
        return ResponseEntity.ok(reportService.generarReporteFacturacion(orderDTO));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<ReportInvoicesDTO>> getByOrderId(@PathVariable Long orderId) throws Exception {
        return ResponseEntity.ok(reportService.getReportsByOrder(orderId));
    }

    @GetMapping("/{estado}")
    public ResponseEntity<List<ReportInvoicesDTO>> getByEstado(@PathVariable String estado) throws Exception {
        return ResponseEntity.ok(reportService.getReportsByEstado(estado));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<ReportInvoicesDTO>> getByFechas(Date inicio, Date fin) throws Exception {
        return ResponseEntity.ok(reportService.getReportsByFechaGeneracion(inicio, fin));
    }

    @PutMapping("/id/estado")
    public ResponseEntity<ReportInvoicesDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) throws Exception {
        return ResponseEntity.ok(reportService.updateReportEstado(id, nuevoEstado));
    }
}