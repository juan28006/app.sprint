package com.mycompany.app.Controller;

import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.service.Interface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<ReportDTO> getAllReports() throws Exception {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) throws Exception {
        ReportDTO reportDTO = reportService.getReportById(id);
        return ResponseEntity.ok(reportDTO);
    }

    @PostMapping
    public ResponseEntity<ReportDTO> createReport(@RequestBody ReportDTO reportDTO) throws Exception {
        ReportDTO createdReport = reportService.createReport(reportDTO);
        return ResponseEntity.ok(createdReport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable Long id,
            @RequestBody ReportDTO reportDTO) throws Exception {
        ReportDTO updatedReport = reportService.updateReport(id, reportDTO);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) throws Exception {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}