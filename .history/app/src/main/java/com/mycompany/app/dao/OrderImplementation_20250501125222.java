package com.mycompany.app.dao;

import com.mycompany.app.dao.interfaces.OrderDao;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.OrderRepository;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Setter
@Getter
public class OrderImplementation implements OrderDao {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        try {
            Order order = Helpers.parse(orderDTO);
            Order savedOrder = orderRepository.save(order);
            return Helpers.parse(savedOrder);
        } catch (Exception e) {
            throw new Exception("Error al crear orden: " + e.getMessage());
        }
    }

    @Override
    public OrderDTO getOrderById(Long id) throws Exception {
        try {
            return orderRepository.findById(id)
                    .map(Helpers::parse)
                    .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al obtener orden: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDTO> getAllOrders() throws Exception {
        try {
            return orderRepository.findAll().stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener todas las órdenes: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) throws Exception {
        try {
            return orderRepository.findByStatus(status).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener órdenes por estado: " + e.getMessage());
        }
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, String status) throws Exception {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + id));
            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);
            return Helpers.parse(updatedOrder);
        } catch (Exception e) {
            throw new Exception("Error al actualizar estado de orden: " + e.getMessage());
        }
    }

    @Override
    public void deleteOrder(Long id) throws Exception {
        try {
            if (!orderRepository.existsById(id)) {
                throw new Exception("Orden no encontrada con ID: " + id);
            }
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar orden: " + e.getMessage());
        }
    }
}