package com.mycompany.app.model;

public class Admin extends Person {
    @Column(nullable = false)
    private String adminCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
