package com.mycompany.app.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.ReportDao;
import com.mycompany.app.dao.repositories.ReportRepository;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.model.Report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
public class ReportImplementation implements ReportDao {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<ReportDTO> getAllReports() throws Exception {
        try {
            return reportRepository.findAll().stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los reportes: " + e.getMessage());
        }
    }

    @Override
    public ReportDTO getReportById(Long id) throws Exception {
        try {
            Optional<Report> report = reportRepository.findById(id);
            return report.map(Helpers::parse)
                    .orElseThrow(() -> new Exception("Reporte no encontrado con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al obtener reporte por ID: " + e.getMessage());
        }
    }

    @Override
    public ReportDTO createReport(ReportDTO reportDTO) throws Exception {
        Report report = Helpers.parse(reportDTO);
        Report savedReport = reportRepository.save(report);
        return Helpers.parse(savedReport);
    }

    @Override
    public ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception {
        try {
            Report existingReport = reportRepository.findById(id)
                    .orElseThrow(() -> new Exception("Reporte no encontrado con ID: " + id));

            existingReport.setType(reportDTO.getType());
            existingReport.setGenerationDate(reportDTO.getGenerationDate());
            existingReport.setUser(Helpers.parse(reportDTO.getUser()));

            Report updatedReport = reportRepository.save(existingReport);
            return Helpers.parse(updatedReport);
        } catch (Exception e) {
            throw new Exception("Error al actualizar reporte: " + e.getMessage());
        }
    }

    @Override
    public void deleteReport(Long id) throws Exception {
        try {
            if (!reportRepository.existsById(id)) {
                throw new Exception("Reporte no encontrado con ID: " + id);
            }
            reportRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar reporte: " + e.getMessage());
        }
    }
}