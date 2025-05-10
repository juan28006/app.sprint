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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTO getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public MachineryDTO getMachinery() {
        return this.machinery;
    }

    public void setMachinery(MachineryDTO machinery) {
        this.machinery = machinery;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

}