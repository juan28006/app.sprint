package com.mycompany.app.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private String orderNumber;
    private Date orderDate;
    private String status;// pendiente, aprobada, cancelada
    private String name;
    private UserDTO createdBy;
    private InventoryDTO inventory;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private List<ReportInvoicesDTO> invoices;
}
