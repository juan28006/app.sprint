package com.mycompany.app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * @author CLAUDIA
 * @version 1.0
 * @created
 */
@Entity
@Table(name = "employee")
public class Employee {
    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private String department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
