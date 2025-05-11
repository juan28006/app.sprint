package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.ReportDTO;
import java.util.List;

public interface ReportService {

    List<ReportDTO> getAllReports() throws Exception;

    ReportDTO getReportById(Long id) throws Exception;

    ReportDTO createReport(ReportDTO reportDTO) throws Exception;

    ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception;

    void deleteReport(Long id) throws Exception;

}