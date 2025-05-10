package com.mycompany.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Entity
@Table(name = "Person")
@Getter
@Setter
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, nullable = false)
    private Long document;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long cellphone;

}
