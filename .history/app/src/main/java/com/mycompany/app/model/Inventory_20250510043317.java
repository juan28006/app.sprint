package com.mycompany.app.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Inventory")
@Getter
@Setter

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventory") // Ajustar según BD real
    private Long id;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "Name", nullable = false)
    private String name; // Cambiado de String nombre a String name

    @Column(name = "status_inventory", nullable = false)
    private String status; // "Operativo", "En Mantenimiento", "Inactiva"

    @ManyToOne
    @JoinColumn(name = "Id_User", nullable = false)
    private User user; // Cambiado de usuario a user

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    @JsonIgnore // Evitar serialización de esta propiedad
    private List<Machinery> machineries;
}
