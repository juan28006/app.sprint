package com.mycompany.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.sql.Date;

import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.UserDTO;

@Getter
@Setter
@NoArgsConstructor
public class ReportInvoicesDTO {
    private Long id;
    private String codigoFactura;
    private Double total;
    private Date fechaGeneracion;
    private OrderDTO order;
    private UserDTO usuario;
    private String estado; // "Pagada", "Pendiente", "Cancelada"

}
