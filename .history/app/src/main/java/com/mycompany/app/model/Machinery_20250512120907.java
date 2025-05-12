package com.mycompany.app.model;

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
@Table(name = "Machinery")
@Getter
@Setter

public class Machinery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Asegúrate que coincida con la BD
    private Long id;

    private String name;

    private String status; // "disponible", "En Mantenimiento", "Dañada"

    @ManyToOne
    @JoinColumn(name = "id_inventario") // Nombre exacto de la columna FK
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}