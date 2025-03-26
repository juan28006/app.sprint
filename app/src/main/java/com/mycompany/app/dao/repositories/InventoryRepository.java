package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Buscar inventario por estado
    List<Inventory> findByStatus(String status);

    // Buscar inventario por fecha de ingreso
    List<Inventory> findByEntryDate(Date entryDate);

    // Buscar inventario por usuario asociado
    List<Inventory> findByUser_Id(Long userId);
}