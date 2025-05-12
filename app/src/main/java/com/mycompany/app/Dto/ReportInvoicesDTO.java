package com.mycompany.app.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportInvoicesDTO {
    private Long id;
    private String codigoFactura;
    private Date fechaGeneracion;
    private OrderDTO order;
    private UserDTO usuario;
    private String estado; // Pendiente,pagada,cancelada
    private Double total;

}
