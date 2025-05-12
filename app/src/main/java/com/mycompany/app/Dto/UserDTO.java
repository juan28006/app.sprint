package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String Username;
    private String password;
    private TypeUserDTO typeUser;
    private PersonDTO person;
    private boolean authenticated;

}
