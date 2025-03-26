package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.Dto.OrderDTO;

public class OrderDao {

    OrderDTO createOrder(OrderDTO orderDTO) throws Exception;

    OrderDTO getOrderById(Long id) throws Exception;

    List<OrderDTO> getAllOrders() throws Exception;

    List<OrderDTO> getOrdersByStatus(String status) throws Exception;

    OrderDTO updateOrderStatus(Long id, String status) throws Exception;

    void deleteOrder(Long id) throws Exception;

}
