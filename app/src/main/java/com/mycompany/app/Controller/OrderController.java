package com.mycompany.app.Controller;

import com.mycompany.app.Dto.OrderDTO;

import com.mycompany.app.service.Interface.OrderService;
import com.mycompany.app.Controller.Validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")

public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidator orderValidator;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) throws Exception {
        orderValidator.validateOrderCreation(orderDTO);
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) throws Exception {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() throws Exception {
        return orderService.getAllOrders();
    }

    @GetMapping("/status/{status}")
    public List<OrderDTO> getOrdersByStatus(@PathVariable String status) throws Exception {
        return orderService.getOrdersByStatus(status);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) throws Exception {
        orderValidator.validateStatusChange(status);
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) throws Exception {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}