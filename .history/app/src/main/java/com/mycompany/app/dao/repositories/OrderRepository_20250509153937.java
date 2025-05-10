package com.mycompany.app.dao.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findByCreatedById(Long userId);

    List<Order> findByMachineryId(Long machineryId);

    InventoryDTO getInventoryByMachineryId(Long machineryId) throws Exception;

    Optional<Order> findByInventoryId(Long inventoryId);

}
