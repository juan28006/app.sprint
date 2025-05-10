package com.mycompany.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Entity
@Table(name = "report_facturacion")
@Getter
@Setter

public class ReportInvoices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoFactura;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Date fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Relación con la orden asociada

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario; // Usuario que genera el reporte

    @Column(nullable = false)
    private String estado; // "Pagada", "Pendiente", "Cancelada"
}
