/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.mycompany.app.Controller.Request.EmployeeRegistrationRequest;
import com.mycompany.app.dao.interfaces.PersonDao;
import com.mycompany.app.dao.interfaces.UserDao;

public class EmployeeValidator extends CommonsValidator {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PersonDao personDao;

    public void validateForCreation(EmployeeRegistrationRequest request) throws Exception {
        if (request == null) {
            throw new Exception("La solicitud de registro no puede ser nula");
        }

        validatePersonData(request);
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());

        // Validación específica para empleados
        if (request.getDepartment() == null || request.getDepartment().trim().isEmpty()) {
            throw new Exception("El departamento es requerido para empleados");
        }
    }

    private void validatePersonData(EmployeeRegistrationRequest request) throws Exception {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new Exception("El nombre es requerido");
        }

        if (request.getDocument() == null) {
            throw new Exception("El documento es requerido");
        }

        if (personDao.existsByDocument(request.getDocument())) {
            throw new Exception("Ya existe una persona con este documento");
        }

        if (request.getCellphone() == null) {
            throw new Exception("El celular es requerido");
        }
    }

    private void validateUsername(String username) throws Exception {
        // Mismas validaciones que AdminValidator
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("El nombre de usuario es requerido");
        }

        if (userDao.existsByUsername(username)) {
            throw new Exception("El nombre de usuario ya está en uso");
        }
    }

    private void validatePassword(String password) throws Exception {
        // Mismas validaciones que AdminValidator
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("La contraseña es requerida");
        }

        if (password.length() < 8) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres");
        }
    }

}
