/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserValidator extends CommonsValidator {
    public void validateUser(UserDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
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