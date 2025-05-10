package com.mycompany.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Inventory")
@Getter
@Setter

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Inventario")
    private Long id; // Cambiado de int idInventario a Long id

    @Column(name = "Fecha_Ingreso", nullable = false)
    private Date entryDate; // Cambiado de fechaIngreso a entryDate

    @Column(name = "Estado_Inventario", nullable = false)
    private String status; // Cambiado de estadoInventario a status

    @ManyToOne
    @JoinColumn(name = "Id_Usuario", nullable = false)
    private User user; // Cambiado de usuario a user

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    // Relacion bidireccional
    // Navegación completa: Puedes acceder en ambas direcciones

    // inventory.getMachineries()

    // machinery.getInventory()Navegación completa: Puedes acceder en ambas
    // direcciones

    // inventory.getMachineries()

    // machinery.getInventory()
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private List<Machinery> machineries;
}
