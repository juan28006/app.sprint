package com.mycompany.app.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.dto.UserDTO;

import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private String orderNumber;
    private Date orderDate;
    private String status;
    private UserDTO createdBy;
    private MachineryDTO machinery;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}