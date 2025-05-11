package com.mycompany.app.Controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Validator.ReportInvoicesValidator;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.service.Interface.ReportInvoicesService;

@RestController
@RequestMapping("/api/reports-invoices")
public class ReportInvoicesController {

    @Autowired
    private ReportInvoicesService reportService;

    @Autowired
    private ReportInvoicesValidator validator;

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
}