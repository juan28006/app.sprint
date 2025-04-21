package com.mycompany.app.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mycompany.app.Dto.PermissionDTO;

@Setter
@Getter
@NoArgsConstructor

public class UserPermissionDTO {
    private Long id;
    private UserDTO user;
    private PermissionDTO permission;

}
