package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.TypeUserDTO;
import org.springframework.stereotype.Component;

@Component
public class TypeUserValidator {
    public void validateTypeUser(TypeUserDTO typeUserDTO) throws Exception {
        if (typeUserDTO.getType() == null || typeUserDTO.getType().isEmpty()) {
            throw new Exception("El tipo de usuario no puede estar vacío");
        }
    }
}
