package com.mycompany.app.Controller.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dto.UserDTO;

@Component
public class UserValidator extends CommonsValidator {

    @Autowired
    private TypeUserValidator typeUserValidator;

    @Autowired
    private UserDao userDao;

    public void validate(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }

        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es requerido");
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new Exception("La contraseña es requerida");
        }

        // Validar TypeUser si está presente
        if (userDTO.getTypeUser() != null) {
            typeUserValidator.validate(userDTO.getTypeUser());
        }
    }

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

    public void validateForCreation(UserDTO userDTO) throws Exception {
        // Validaciones básicas
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }

        validateUsername(userDTO.getUsername());
        validatePassword(userDTO.getPassword());
    }

    private void validateUsername(String username) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("El nombre de usuario es requerido");
        }

        if (username.length() < 4 || username.length() > 20) {
            throw new Exception("El nombre de usuario debe tener entre 4 y 20 caracteres");
        }

        if (!username.matches("^[a-zA-Z0-9._-]+$")) {
            throw new Exception(
                    "El nombre de usuario solo puede contener letras, números, puntos, guiones y guiones bajos");
        }

        if (userDao.existsByUsername(username)) {
            throw new Exception("El nombre de usuario ya está en uso");
        }
    }

    private void validatePassword(String password) throws Exception {
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("La contraseña es requerida");
        }

        if (password.length() < 8) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres");
        }

        // Opcional: Validar complejidad de contraseña
        if (!password.matches(".*[A-Z].*")) {
            throw new Exception("La contraseña debe contener al menos una mayúscula");
        }
        if (!password.matches(".*\\d.*")) {
            throw new Exception("La contraseña debe contener al menos un número");
        }

        // Validar que la contraseña no se repita (opcional)
        if (userDao.existsByPassword(password)) {
            throw new Exception("La contraseña no es segura (ya está en uso por otro usuario)");
        }
    }
}