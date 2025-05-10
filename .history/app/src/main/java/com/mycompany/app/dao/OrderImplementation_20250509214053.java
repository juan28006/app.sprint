package com.mycompany.app.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.interfaces.OrderDao;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dao.repositories.OrderRepository;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.UserDTO;
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        try {
            // Asignar valores por defecto si no están presentes
            if (orderDTO.getCreatedBy() == null || orderDTO.getCreatedBy().getId() == null) {
                throw new Exception("Debe especificar un usuario creador");
            }

            if (orderDTO.getInventory() == null || orderDTO.getInventory().getId() == null) {
                throw new Exception("Debe especificar un inventario");
            }

            if (orderDTO.getName() == null || orderDTO.getName().trim().isEmpty()) {
                throw new Exception("El nombre de la máquina es obligatorio");
            }

            // Verificar que el usuario existe
            UserDTO user = userDao.getUserById(orderDTO.getCreatedBy().getId());
            if (user == null) {
                throw new Exception("No se encontró usuario con ID: " + orderDTO.getCreatedBy().getId());
            }

            // Verificar que el inventario existe
            InventoryDTO inventory = inventoryDao.getInventoryById(orderDTO.getInventory().getId());
            if (inventory == null) {
                throw new Exception("No se encontró inventario con ID: " + orderDTO.getInventory().getId());
            }

            Order order = Helpers.parse(orderDTO);

            // Calcular total si no viene
            if (order.getTotalPrice() == null && order.getUnitPrice() != null && order.getQuantity() != null) {
                order.setTotalPrice(order.getUnitPrice() * order.getQuantity());
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
