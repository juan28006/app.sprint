package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.TypeUserDTO;
import java.util.List;

public interface TypeUserDao {

    List<TypeUserDTO> getAllTypeUsers() throws Exception;

    TypeUserDTO getTypeUserById(Long id) throws Exception;

    TypeUserDTO createTypeUser(TypeUserDTO typeUserDTO) throws Exception;

    TypeUserDTO updateTypeUser(Long id, TypeUserDTO typeUserDTO) throws Exception;

    void deleteTypeUser(Long id) throws Exception;
}