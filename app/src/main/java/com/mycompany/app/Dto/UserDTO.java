package com.mycompany.app.Dto;

public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private TypeUserDTO typeUser;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypeUserDTO getTypeUser() {
        return this.typeUser;
    }

    public void setTypeUser(TypeUserDTO typeUser) {
        this.typeUser = typeUser;
    }

}
