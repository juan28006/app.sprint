/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.TypeUserDTO;
import com.mycompany.app.Dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class TypeUserValidator {
    public void validateTypeUser(TypeUserDTO typeUserDTO) {
        if (typeUserDTO.getType() == null || typeUserDTO.getType().isEmpty()) {
            throw new IllegalArgumentException("El tipo de usuario no puede estar vacío");
        }
    }
}
