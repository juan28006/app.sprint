package com.mycompany.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Admin")
@Setter
@Getter
@NoArgsConstructor
public class Admin extends Person {
    @Column(nullable = false)
    private String department;

}
