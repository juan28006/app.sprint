package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.OrderDTO;

public interface OrderDao {
    OrderDTO createOrder(OrderDTO orderDTO) throws Exception;

    OrderDTO getOrderById(Long id) throws Exception;

    List<OrderDTO> getOrdersByStatus(String status) throws Exception;

    OrderDTO updateOrderStatus(Long id, String status) throws Exception;

    void deleteOrder(Long id) throws Exception;

    List<OrderDTO> getOrdersByInventoryId(Long inventoryId) throws Exception;

}
