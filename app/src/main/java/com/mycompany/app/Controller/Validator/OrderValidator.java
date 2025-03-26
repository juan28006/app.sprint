package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.OrderDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderValidator extends CommonsValidator {

    public void validateOrderCreation(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            throw new Exception("La orden no puede ser nula");
        }
        if (orderDTO.getQuantity() == null || orderDTO.getQuantity() <= 0) {
            throw new Exception("La cantidad debe ser mayor que cero");
        }
        if (orderDTO.getUnitPrice() == null || orderDTO.getUnitPrice() <= 0) {
            throw new Exception("El precio unitario debe ser mayor que cero");
        }
        if (orderDTO.getMachinery() == null || orderDTO.getMachinery().getId() == null) {
            throw new Exception("Debe especificar la maquinaria");
        }
    }

    public void validateStatusChange(String status) throws Exception {
        List<String> validStatuses = List.of("Pendiente", "Aprobada", "Rechazada", "Completada");
        if (!validStatuses.contains(status)) {
            throw new Exception("Estado no válido. Valores permitidos: " + validStatuses);
        }
    }
}