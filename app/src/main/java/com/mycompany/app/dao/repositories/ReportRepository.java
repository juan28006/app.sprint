package com.mycompany.app.dao.repositories;

import java.util.Date;

import com.mycompany.app.model.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByType(String type);

    List<Report> findByUserId_Id(Long userId);

    List<Report> findByGenerationDateBetween(Date startDate, Date endDate);
}