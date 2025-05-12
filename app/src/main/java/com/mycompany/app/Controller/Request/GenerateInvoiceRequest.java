package com.mycompany.app.Controller.Request;

import com.mycompany.app.dto.OrderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenerateInvoiceRequest {
    private OrderDTO orderDTO;

}
