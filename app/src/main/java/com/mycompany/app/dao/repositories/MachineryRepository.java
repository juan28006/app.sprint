package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Machinery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MachineryRepository extends JpaRepository<Machinery, Long> {
    List<Machinery> findByStatus(String status);

    List<Machinery> findByInventory_Id(Long inventoryId);

    List<Machinery> findByNameContaining(String name);
}