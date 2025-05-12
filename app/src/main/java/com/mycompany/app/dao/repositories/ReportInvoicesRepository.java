package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.ReportInvoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ReportInvoicesRepository extends JpaRepository<ReportInvoices, Long> {

    List<ReportInvoices> findByOrderId(Long orderId);

    List<ReportInvoices> findByEstado(String estado);

    List<ReportInvoices> findByFechaGeneracionBetween(Date inicio, Date fin);

    List<ReportInvoices> findByUsuarioId(Long userId);
}