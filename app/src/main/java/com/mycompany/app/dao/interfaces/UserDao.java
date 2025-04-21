package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.UserDTO;
import java.util.List;

public interface UserDao {
    List<UserDTO> getAllUsers() throws Exception;

    UserDTO getUserById(Long id) throws Exception;

    UserDTO createUser(UserDTO userDTO) throws Exception;

    UserDTO updateUser(Long id, UserDTO userDTO) throws Exception;

    void deleteUser(Long id) throws Exception;

    boolean existsByUsername(String username) throws Exception;

    UserDTO findByUsername(String username) throws Exception;
}