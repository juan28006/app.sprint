package com.mycompany.app.Controller.Validator;

import org.springframework.stereotype.Component;

import com.mycompany.app.dto.UserDTO;

@Component
public class UserValidator extends CommonsValidator {

    public void validateUser(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (userDTO.getTypeUser() == null || userDTO.getTypeUser().getId() == null) {
            throw new IllegalArgumentException("El tipo de usuario no puede estar vacío");
        }
    }
}