package com.mycompany.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Report")
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "Uso", "Estado", "Mantenimiento"
    private Date generationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}