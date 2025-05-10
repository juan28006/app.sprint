package com.mycompany.app.dao.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.app.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findByCreatedById(Long userId);

    Optional<Order> findByInventoryId(Long inventoryId);

    Optional<Order> findByName(String name);  // Usar exactamente el mismo nombre
}
