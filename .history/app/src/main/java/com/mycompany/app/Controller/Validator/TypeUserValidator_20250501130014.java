package com.mycompany.app.Controller.Validator;

import org.springframework.stereotype.Component;

import com.mycompany.app.dto.TypeUserDTO;

@Component
public class TypeUserValidator {
    public void validateTypeUser(TypeUserDTO typeUserDTO) throws Exception {
        if (typeUserDTO.getType() == null || typeUserDTO.getType().isEmpty()) {
            throw new Exception("El tipo de usuario no puede estar vacío");
        }
    }
}
