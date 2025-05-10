package com.mycompany.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author CLAUDIA
 * @version 1.0
 * @created
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Employee extends Person {

    @Column(nullable = false)
    private String department;

}
