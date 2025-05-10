package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.ReportDTO;

public interface ReportDao {

    ReportDTO createReport(ReportDTO reportDTO) throws Exception;

    ReportDTO getReportById(Long id) throws Exception;

    List<ReportDTO> getAllReports() throws Exception;

    ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception;

    void deleteReport(Long id) throws Exception;
}