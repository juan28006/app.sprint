package com.mycompany.app.Dto;

import lombok.NoArgsConstructor;
import com.mycompany.app.Dto.TypeUserDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String Username;
    private String password;
    private TypeUserDTO typeUser;
    private boolean authenticated;

}
