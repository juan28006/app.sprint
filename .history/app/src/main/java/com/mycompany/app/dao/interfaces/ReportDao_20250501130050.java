package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.ReportDTO;

public interface ReportDao {

    List<ReportDTO> getAllReports() throws Exception;

    ReportDTO getReportById(Long id) throws Exception;

    ReportDTO createReport(ReportDTO reportDTO) throws Exception;

    ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception;

    void deleteReport(Long id) throws Exception;
}