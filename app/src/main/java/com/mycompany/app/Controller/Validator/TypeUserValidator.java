package com.mycompany.app.Controller.Validator;

import org.springframework.stereotype.Component;

import com.mycompany.app.dto.TypeUserDTO;

@Component
public class TypeUserValidator {
    public void validate(TypeUserDTO typeUserDTO) throws Exception {
        if (typeUserDTO == null) {
            throw new Exception("El DTO de tipo de usuario no puede ser nulo");
        }
        if (typeUserDTO.getType() == null || typeUserDTO.getType().trim().isEmpty()) {
            throw new Exception("El tipo no puede estar vacío");
        }
        // Permisos ahora puede ser nulo porque se asignará un valor por defecto
    }

    private void validateNotNull(TypeUserDTO typeUserDTO) throws Exception {
        if (typeUserDTO == null) {
            throw new Exception("El DTO de tipo de usuario no puede ser nulo");
        }
    }

    private void validateType(String type) throws Exception {
        if (type == null || type.trim().isEmpty()) {
            throw new Exception("El tipo de usuario no puede estar vacío");
        }

        if (!type.matches("^[a-zA-Z0-9_\\- ]+$")) {
            throw new Exception(
                    "El tipo de usuario solo puede contener letras, números, espacios, guiones y guiones bajos");
        }

        if (type.length() > 50) {
            throw new Exception("El tipo de usuario no puede exceder los 50 caracteres");
        }
    }

    private void validatePermissions(String permissions) throws Exception {
        if (permissions == null) {
            throw new Exception("Los permisos no pueden ser nulos");
        }

        if (permissions.length() > 1000) {
            throw new Exception("Los permisos no pueden exceder los 1000 caracteres");
        }

        // Validar formato de permisos (separados por comas sin espacios)
        if (!permissions.matches("^[a-zA-Z0-9_\\-,]+$")) {
            throw new Exception(
                    "Los permisos deben estar separados por comas y solo contener caracteres alfanuméricos y guiones bajos");
        }
    }
}
