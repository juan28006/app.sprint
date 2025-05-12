package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.UserDTO;

public interface UserService {
    List<UserDTO> getAllUsers() throws Exception;

    UserDTO getUserById(Long id) throws Exception;

    UserDTO createUser(UserDTO userDTO) throws Exception; // Crear usuario genérico

    UserDTO createAdmin(UserDTO userDTO) throws Exception; // Crear admin

    UserDTO createEmpleado(UserDTO userDTO) throws Exception; // Crear empleado

    UserDTO createCliente(UserDTO userDTO) throws Exception; // Crear cliente

    UserDTO updateUser(Long id, UserDTO userDTO) throws Exception;

    void deleteUser(Long id) throws Exception;

    void deletePerson(PersonDTO personDTO) throws Exception;
}