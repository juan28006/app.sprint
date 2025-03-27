package com.mycompany.app.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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