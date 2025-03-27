package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByMachineryId(Long machineryId); // Añade este método
}