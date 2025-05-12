package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.TypeUserDTO;

public interface TypeUserService {
    List<TypeUserDTO> getAllTypeUsers() throws Exception;

    TypeUserDTO getTypeUserById(Long id) throws Exception;

    TypeUserDTO createTypeUser(TypeUserDTO typeUserDTO) throws Exception;

    TypeUserDTO updateTypeUser(Long id, TypeUserDTO typeUserDTO) throws Exception;

    void deleteTypeUser(Long id) throws Exception;
}