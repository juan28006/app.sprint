package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class UserPermissionDTO {
    private Long id;
    private UserDTO user;
    private PermissionDTO permission;

}
