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
        try {
            // Validar que el nombre de la máquina no sea nulo
            if (orderDTO.getName_maquina() == null || orderDTO.getName_maquina().trim().isEmpty()) {
                throw new Exception("El nombre de la máquina es obligatorio");
            }
            Order order = Helpers.parse(orderDTO);

            // Validar que el totalPrice viene calculado
            if (order.getTotalPrice() == null) {
                throw new Exception("El precio total no fue calculado");
            }

            Order savedOrder = orderRepository.save(order);
            return Helpers.parse(savedOrder);
        } catch (Exception e) {
            throw new Exception("Error al crear orden: " + e.getMessage());
        }
    }

    @Override
    public OrderDTO getOrderById(Long id) throws Exception {
        return orderRepository.findById(id)
                .map(Helpers::parse)
                .orElseThrow(() -> new Exception("Orden no encontrada con ID: " + id));
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) throws Exception {
        return orderRepository.findByStatus(status).stream()
                .map(Helpers::parse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByNameMaquina(String nameMaquina) throws Exception {
        try {
            return orderRepository.findByName(nameMaquina).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener órdenes por nombre de máquina: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDTO> getOrdersByInventoryId(Long inventoryId) throws Exception {
        try {
            return orderRepository.findByInventoryId(inventoryId).stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener órdenes por ID de inventario: " + e.getMessage());
        }
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
}
