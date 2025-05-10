package com.mycompany.app.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    private Date fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Relación con la orden asociada

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario; // Usuario que genera el reporte

    @Column(nullable = false)
    private String estado; // "Pagada", "Pendiente", "Cancelada"

    @Column(nullable = false)
    private Double total;

}
