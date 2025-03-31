package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Spring generará automáticamente la consulta basada en el nombre del método
    List<Inventory> findByMachineries_Id(Long machineryId);
}