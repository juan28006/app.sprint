package com.mycompany.app.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.OrderDao;
import com.mycompany.app.dao.repositories.OrderRepository;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.model.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
public class OrderImplementation implements OrderDao {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        Order order = Helpers.parse(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return Helpers.parse(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long id) throws Exception {
        return orderRepository.findById(id)
                .map(Helpers::parse)
                .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + id));
    }

    @Override
    public List<OrderDTO> getAllOrders() throws Exception {
        return orderRepository.findAll().stream()
                .map(Helpers::parse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) throws Exception {
        return orderRepository.findByStatus(status).stream()
                .map(Helpers::parse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, String status) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + id));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return Helpers.parse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) throws Exception {
        if (!orderRepository.existsById(id)) {
            throw new Exception("Orden no encontrada con ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public void approveOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + orderId));

        order.setStatus("Aprobada");
        orderRepository.save(order);
    }

    @Override
    public void rejectOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + orderId));

        if (!"Pendiente".equals(order.getStatus())) {
            throw new Exception("Solo se pueden rechazar órdenes en estado 'Pendiente'");
        }

        order.setStatus("Rechazada");
        orderRepository.save(order);
    }

}