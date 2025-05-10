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
import com.mycompany.app.model.Inventory;
import com.mycompany.app.model.Order;
import com.mycompany.app.model.User;

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
            // Validación básica del DTO
            if (orderDTO == null) {
                throw new Exception("El objeto OrderDTO no puede ser nulo");
            }

            // Validar campos obligatorios
            if (orderDTO.getName() == null || orderDTO.getName().trim().isEmpty()) {
                throw new Exception("El nombre de la máquina es obligatorio");
            }

            // Validar usuario creador - más estricto
            if (orderDTO.getCreatedBy() == null) {
                throw new Exception("El usuario creador es obligatorio");
            }
            if (orderDTO.getCreatedBy().getId() == null) {
                throw new Exception("El ID del usuario creador es obligatorio");
            }

            // Validar inventario - más estricto
            if (orderDTO.getInventory() == null) {
                throw new Exception("El inventario asociado es obligatorio");
            }
            if (orderDTO.getInventory().getId() == null) {
                throw new Exception("El ID del inventario es obligatorio");
            }

            // Verificar existencia del usuario
            UserDTO userDTO = userDao.getUserById(orderDTO.getCreatedBy().getId());
            if (userDTO == null) {
                throw new Exception("No se encontró usuario con ID: " + orderDTO.getCreatedBy().getId());
            }

            // Verificar existencia del inventario
            InventoryDTO inventoryDTO = inventoryDao.getInventoryById(orderDTO.getInventory().getId());
            if (inventoryDTO == null) {
                throw new Exception("No se encontró inventario con ID: " + orderDTO.getInventory().getId());
            }

            // Calcular total si no viene
            if (orderDTO.getTotalPrice() == null) {
                if (orderDTO.getUnitPrice() != null && orderDTO.getQuantity() != null) {
                    orderDTO.setTotalPrice(orderDTO.getUnitPrice() * orderDTO.getQuantity());
                } else {
                    throw new Exception("No se puede calcular el total: faltan precio unitario o cantidad");
                }
            }

            // Crear la entidad Order directamente sin usar Helpers.parse
            Order order = new Order();
            order.setName(orderDTO.getName());
            order.setQuantity(orderDTO.getQuantity());
            order.setUnitPrice(orderDTO.getUnitPrice());
            order.setTotalPrice(orderDTO.getTotalPrice());
            order.setStatus("Pendiente"); // Estado por defecto

            // Crear y asignar User solo con el ID
            User user = new User();
            user.setId(userDTO.getId());
            order.setCreatedBy(user);

            // Crear y asignar Inventory solo con el ID
            Inventory inventory = new Inventory();
            inventory.setId(inventoryDTO.getId());
            order.setInventory(inventory);

            // Generar valores automáticos
            order.setOrderNumber("ORD-" + System.currentTimeMillis());
            order.setOrderDate(new java.util.Date());

            Order savedOrder = orderRepository.save(order);
            return parse(savedOrder);
        } catch (Exception e) {
            throw new Exception("Error al crear orden: " + e.getMessage(), e);
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
