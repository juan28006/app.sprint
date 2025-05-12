package com.mycompany.app.Controller.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// POST /api/orders → Crear orden
@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {
    private Long inventoryId;

    private Integer quantity;

}
