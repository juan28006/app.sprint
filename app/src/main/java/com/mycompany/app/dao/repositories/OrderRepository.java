package com.mycompany.app.dao.repositories;

import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findByCreatedById(Long userId);

    List<Order> findByMachineryId(Long machineryId);

    InventoryDTO getInventoryByMachineryId(Long machineryId) throws Exception;

}
