package com.mycompany.app.model;

import com.mycompany.app.model.Inventory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Machinery")
@Getter
@Setter

public class Machinery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String status; // "disponible", "En Mantenimiento", "Dañada"

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;
}