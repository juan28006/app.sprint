package com.mycompany.app.Dto;

public class UserPermissionDTO {
    private Long id;
    private UserDTO user;
    private PermissionDTO permission;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return this.user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PermissionDTO getPermission() {
        return this.permission;
    }

    public void setPermission(PermissionDTO permission) {
        this.permission = permission;
    }

}
