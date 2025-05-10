package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.ReportDTO;
import java.util.List;

public interface ReportDao {

    List<ReportDTO> getAllReports() throws Exception;

    ReportDTO getReportById(Long id) throws Exception;

    ReportDTO createReport(ReportDTO reportDTO) throws Exception;

    ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception;

    void deleteReport(Long id) throws Exception;
}