package com.mycompany.app.Controller.Validator;

import org.springframework.stereotype.Component;

import com.mycompany.app.dto.UserDTO;

@Component
public class UserValidator extends CommonsValidator {

    public void validateUser(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            throw new Exception("El nombre de usuario no puede estar vacío");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía");
        }
        if (userDTO.getTypeUser() == null) {
            throw new Exception("El tipo de usuario no puede estar vacío");
        }
        if (userDTO.getTypeUser().getType() == null || userDTO.getTypeUser().getType().isEmpty()) {
            throw new Exception("El tipo de usuario debe especificarse");
        }
    }
}