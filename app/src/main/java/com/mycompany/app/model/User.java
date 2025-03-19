package com.mycompany.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "User")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;

    @ManyToOne
    @JoinColumn(name = "type_user_id", nullable = false)
    private TypeUser typeUser;
}