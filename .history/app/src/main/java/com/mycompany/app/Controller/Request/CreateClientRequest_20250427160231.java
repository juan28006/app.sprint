package com.mycompany.app.Controller.Request;

import com.mycompany.app.Dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateClientRequest {
    private UserDTO userDTO;

}
