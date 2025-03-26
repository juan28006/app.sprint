package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.Dto.OrderDTO;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO) throws Exception;

    OrderDTO getOrderById(Long id) throws Exception;

    List<OrderDTO> getAllOrders() throws Exception;

    List<OrderDTO> getOrdersByStatus(String status) throws Exception;

    OrderDTO updateOrderStatus(Long id, String status) throws Exception;

    void deleteOrder(Long id) throws Exception;

    void approveOrder(Long orderId) throws Exception;

    void rejectOrder(Long orderId) throws Exception;

}
