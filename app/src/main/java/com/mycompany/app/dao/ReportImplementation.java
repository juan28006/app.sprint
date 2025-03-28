package com.mycompany.app.dao;

import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.dao.interfaces.ReportDao;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.ReportRepository;
import com.mycompany.app.model.Report;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter
public class ReportImplementation implements ReportDao {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<ReportDTO> getAllReports() {
        List<Report> reportList = reportRepository.findAll();
        List<ReportDTO> reportDTOList = new ArrayList<>();

        for (Report report : reportList) {
            reportDTOList.add(Helpers.parse(report));
        }

        return reportDTOList;
    }

    @Override
    public ReportDTO getReportById(Long id) {
        Optional<Report> optionalReport = reportRepository.findById(id);
        return optionalReport.map(Helpers::parse).orElse(null);
    }

    @Override
    public ReportDTO createReport(ReportDTO reportDTO) {
        Report report = Helpers.parse(reportDTO);
        Report savedReport = reportRepository.save(report);
        return Helpers.parse(savedReport);
    }

    @Override
    public ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception {
        if (!reportRepository.existsById(id)) {
            throw new Exception("Report not found with id: " + id);
        }

        reportDTO.setId(id);
        Report report = Helpers.parse(reportDTO);
        Report updatedReport = reportRepository.save(report);
        return Helpers.parse(updatedReport);
    }

    @Override
    public void deleteReport(Long id) throws Exception {
        if (!reportRepository.existsById(id)) {
            throw new Exception("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }
}